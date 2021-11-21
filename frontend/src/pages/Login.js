import { useContext, useState } from 'react';
import { Form, Input, Button, Typography } from 'antd';
import { Context } from '../store';
import { loginUser } from '../store/actions';

const Login = () => {
  const [userName, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [state, dispatch] = useContext(Context)

  const { Title, Text } = Typography;

  const handleSubmit = async () => {
    const userData = {
      username: userName,
      password: password
    }

    console.log(userData)

    const res = await fetch('http://localhost:8080/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userData),
    })
    
    const returnData = await res.json()

    if(returnData.access_token) {
      console.log("Successfully logged in")
      dispatch(loginUser(returnData))
    } else {
      let errors = ''
        if (returnData.error) {
          errors = returnData.error
        } else {
          for (let i = 0; i < returnData.msg.length; i++) {
            errors += returnData.msg[i].param[0].toUpperCase() + returnData.msg[i].param.slice(1) + ' ' + returnData.msg[i].msg + '\n'
          }
        }
        setError(errors)
    }
  }

  const handleError = (err) => {
    console.log(err)
  }

  return(
    <>
    { !state.auth.token &&
      (
        <>
          <Title style={{textAlign: 'center'}}>Log In</Title>
          <Form
            name="basic"
            style={{maxWidth: '50%', margin: 'auto'}}
            initialValues={{ remember: true }}
            onFinish={handleSubmit}
            onFinishFailed={handleError}
            autoComplete="off"
          >
            <Form.Item 
              label="Username"
              name="userName"
              required
            >
              <Input
                onChange={e => setUsername(e.target.value)}
              />
            </Form.Item>

            <Form.Item 
              label="Password"
              name="password"
              required
            >
              <Input.Password
                onChange={e => setPassword(e.target.value)}
              />
            </Form.Item>
            { error && <Text style={{whiteSpace: 'pre-wrap'}} type="danger">{ error }</Text> }

            <Form.Item style={{textAlign: 'center'}}>
              <Button type="primary" htmlType="submit">
                Submit
              </Button>
            </Form.Item>

          </Form>
        </>
      )
    }
  </>
  )
}

export default Login;