import {Button, Card, Select,Spin} from 'antd';
import {useContext, useState} from "react";
import {Context} from "../../store";
import {useNavigate} from "react-router-dom";
import Text from "antd/es/typography/Text";
import {ReloadOutlined, UsergroupDeleteOutlined} from "@ant-design/icons";


const ConfigureUserRankDelete = (props) => {
  const [state] = useContext(Context)
  const [user, setUser] = useState(props.user)
  const [error, setError] = useState()
  const [selectedRole, setSelectedRole] = useState()
  const navigate = useNavigate();

  async function tryRemoveRole() {
    try {
      await fetch(`${process.env.REACT_APP_SITE_URL}/api/role/deletefromuser`, {
        method: 'DELETE',
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
    user['roles'].map((role) => {
      if (role.name !== 'ROLE_USER')
        roles.push(<Select.Option key={role['id']} value={role['name']}>{role['displayName']}</Select.Option>)
    })
    return roles
  }

  if (error) return <Text style={{whiteSpace: 'pre-wrap'}} type="danger">{error}</Text>

  return (
    <>
      {(user)
        ?
        <div>
          <Card type="inner" title={"Delete a role from " + user['displayName']}>
            <Select placeholder="Select a role" style={{width: 'auto'}} onChange={handleChange}>
              {setOptionRows()}
            </Select>
          </Card>
          <Card
            style={{margin: '16px 0'}}
            type="inner"
            title="Preview">
            {selectedRole ?
              <>
                {displayRoles(user['roles'].filter(r => r.name !== selectedRole))}<ReloadOutlined
                style={{fontSize: 20, color: 'lightgray'}} onClick={() => setSelectedRole(null)}/>
              </>
              :
              <>{displayRoles(user['roles'])}</>}
          </Card>
          {selectedRole &&
          <Button danger icon={<UsergroupDeleteOutlined/>} onClick={async () => tryRemoveRole()}>Confirm</Button>}
        </div>
        :
        <div style={{display: 'flex', justifyContent: 'center'}}>
          <Spin/>
        </div>
      }
    </>
  )
}

export default ConfigureUserRankDelete;