import {Card, Spin} from 'antd';
import {useContext, useEffect, useState} from "react";
import ConfigureUserRankDelete from "./ConfigureUserRankDelete";
import ConfigureUserRankAdd from "./ConfigureUserRankAdd";
import {Navigate, useParams} from "react-router-dom";
import {Context} from "../../store";

const tabList = [
  {
    key: 'delete',
    tab: 'Delete',
  },
  {
    key: 'add',
    tab: 'Add',
  },
];

const ConfigureUserRankTabs = () => {
  const [state] = useContext(Context)
  const [activeTabKey, setActiveTabKey] = useState('tab1');
  const params = useParams()
  const [user, setUser] = useState()
  const [allRoles, setAllRoles] = useState([])
  const [error, setError] = useState()
  const contentList = {
    delete: <ConfigureUserRankDelete user={user}/>,
    add: <ConfigureUserRankAdd user={user} allRoles={allRoles}/>
  };

  useEffect(async () => {
    await fetchUserRoles()
    await fetchAllRoles()
  }, [])

  async function fetchUserRoles() {
    try {
      setUser(await fetch(`${process.env.REACT_APP_SITE_URL}/api/user/username/${params.username}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + state.auth.token
        }
      })
        .then(res => {
          if (!res.ok) throw new Error('Could not load user.')
          else return res.json()
        }))
    } catch (e) {
      console.log(e)
      setError(e)
    }
  }

  async function fetchAllRoles() {
    try {
      setAllRoles(await fetch(`${process.env.REACT_APP_SITE_URL}/api/roles`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + state.auth.token
        }
      })
        .then(res => {
          if (!res.ok) throw new Error('Could not load roles.')
          return res.json()
        }))
    } catch (e) {
      setError('Could not load roles')
    }
  }

  const onTabChange = key => {
    setActiveTabKey(key);
  };

  if (!state.auth.token || (state.auth.token && !state.auth.user.roles.includes('ROLE_ADMIN'))) {
    return <Navigate to="/"/>
  }

  return (
    <>
      {(user && allRoles.length)
        ?
        <Card
          style={{width: '100%'}}
          title={`Edit ranks of ${params.username}`}
          tabList={tabList}
          activeTabKey={activeTabKey}
          onTabChange={key => {
            onTabChange(key);
          }}
        >
          {contentList[activeTabKey]}
        </Card>
        :
        <div style={{display: 'flex', justifyContent: 'center'}}>
          <Spin/>
        </div>
      }
    </>
  )
}

export default ConfigureUserRankTabs