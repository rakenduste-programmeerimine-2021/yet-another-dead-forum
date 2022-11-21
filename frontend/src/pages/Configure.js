import {Context} from "../store";
import {useContext, useEffect, useState} from "react";
import {Navigate} from "react-router-dom";
import ConfigureLoaded from "../components/Configure/ConfigureLoaded";
import {Spin} from "antd";

const Configure = () => {
  const [state] = useContext(Context)
  const [users, setUsers] = useState()
  const [error, setError] = useState()

  useEffect(async () => {
    try {
      setUsers(await fetch(`${process.env.REACT_APP_SITE_URL}/api/users`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + state.auth.token
        }
      })
        .then(res => {
          if (!res.ok) throw new Error('Could load users.')
          else return res.json()
        }))
    } catch (e) {
      setError('Could not load users')
    }
  }, [])

  if (!state.auth.token || (state.auth.token && !state.auth.user.roles.includes('ROLE_ADMIN'))) {
    return (

      <Navigate to="/"/>
    )
  }

  return (
    <>
      {(!users && !error) && <Spin style={{display: 'flex', justifyContent: 'center'}}/>}
      {
        (users && !error)
          ?
          <ConfigureLoaded users={users}/>
          :
          <p style={{display: 'flex', justifyContent: 'center'}}>{error}</p>
      }
    </>
  )
}

export default Configure;