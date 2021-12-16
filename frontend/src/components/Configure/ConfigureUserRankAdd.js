import {Button, Card, Select, Spin} from 'antd';
import {useContext, useState} from "react";
import {Context} from "../../store";
import {useNavigate} from "react-router-dom";
import Text from "antd/es/typography/Text";
import {CloseCircleOutlined, UsergroupAddOutlined} from "@ant-design/icons";


const ConfigureUserRankAdd = (props) => {
  const [state] = useContext(Context)
  const [user, setUser] = useState(props.user)
  const [allRoles, setAllRoles] = useState(props.allRoles);
  const [error, setError] = useState()
  const [selectedRole, setSelectedRole] = useState()
  const navigate = useNavigate();

  async function tryAddRole() {
    try {
      await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/role/addtouser`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + state.auth.token
        },
        body: JSON.stringify({
          rolename: selectedRole,
          username: user['username']
        })
      })
      navigate("/configure?success=Successfully updated roles for " + user['displayName'])
    } catch (e) {
      setError('Failed to add role')
    }
  }

  function displayRoles(roles) {
    return roles.map(r => <span
      key={r['id']}
      style={{
        textAlign: 'center',
        backgroundColor: r['bodyCss'],
        color: r['textCss'],
        padding: '5px 15px',
        margin: '5px'
      }}
    >{r['displayName']}</span>)
  }

  function handleChange(value) {
    setSelectedRole(value)
  }

  function setOptionRows() {
    let roles = []
    allRoles.map((role) => {
      if (!user['roles'].filter(r => r.name === role.name).length) {
        roles.push(<Select.Option key={role['id']} value={role['name']}>{role['displayName']}</Select.Option>)
      }
    })
    return roles
  }

  if (error) return <Text style={{whiteSpace: 'pre-wrap'}} type="danger">{error}</Text>

  return (
    <>
      {(user && allRoles.length)
        ?
        <div>
          <Card type="inner" title={' Add a role to ' + user['displayName']}>
            <Select placeholder="Select a role" style={{width: 'auto'}} onChange={handleChange}>
              {setOptionRows()}
            </Select>
          </Card>
          <Card
            style={{margin: '16px 0'}}
            type="inner"
            title="Preview">
            {displayRoles(user['roles'])}
            {selectedRole && <>{displayRoles(allRoles.filter(r => r.name === selectedRole))}<CloseCircleOutlined
              style={{fontSize: 20, color: 'lightgray'}} onClick={() => setSelectedRole(null)}/></>}
          </Card>
          {selectedRole && <Button icon={<UsergroupAddOutlined/>} onClick={async () => tryAddRole()}>Confirm</Button>}
        </div>
        :
        <div style={{display: 'flex', justifyContent: 'center'}}>
          <Spin/>
        </div>
      }
    </>
  )
}

export default ConfigureUserRankAdd;