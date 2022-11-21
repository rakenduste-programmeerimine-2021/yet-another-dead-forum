import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Input, Button, Typography } from 'antd';
import { Context } from '../store';
import { loginUser } from '../store/actions';

function Register() {
  const [error, setError] = useState('');
  const [state, dispatch] = useContext(Context);
  const [form] = Form.useForm();
  const navigate = useNavigate();

  const handleSubmit = async (values) => {
    const user = {
      username: values.userName,
      email: values.email,
      password: values.password
    }

    const res = await fetch(`${process.env.REACT_APP_SITE_URL}/api/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
        body: JSON.stringify(user),
    })

    const returnData = await res.json()

    if (res.ok) {
      setError('')
      const loginData = {
        username: values.userName,
        password: values.password
      }

      const response = await fetch(`${process.env.REACT_APP_SITE_URL}/api/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData),
      })
      
      const returnLoginData = await response.json()

      if(returnLoginData.token) {
        dispatch(loginUser(loginData))
        navigate('/', { replace: true });
      } else {
        let errs = ''
        if (returnLoginData.error_message) {
          errs = returnLoginData.error_message
        } else {
          for (let i = 0; i < returnLoginData.msg.length; i++) {
            errs += (returnLoginData.msg[i].param[0].toUpperCase() + returnLoginData.msg[i].param.slice(1) + ' ' +
            returnLoginData.msg[i].msg + '\n')
          }
        }
        setError(errs)
      }
    } else {
      let errors = ''
      if (returnData.error_message) {
        errors = returnData.error_message
      } else {
        for (let i = 0; i < returnData.msg.length; i++) {
          errors += (returnData.msg[i].param[0].toUpperCase() + returnData.msg[i].param.slice(1) + ' ' +
          returnData.msg[i].msg + '\n')
        }
      }
      setError(errors)
    }
  }

  const { Title, Text } = Typography;
  return(
    <>
    { !state.auth.token &&
      (
        <>
          <Title style={{textAlign: 'center'}}>Register</Title>
          <Form
            form={form}
            name="register"
            style={{maxWidth: '50%', margin: 'auto'}}
            initialValues={{ remember: true }}
            onFinish={handleSubmit}
            autoComplete="off"
            scrollToFirstError
          >
            <Form.Item 
              label="Username"
              name="userName"
              rules={[
                {
                  required: true,
                  message: 'Please input your username!',
                  whitespace: true,
                },
                {
                  pattern: /^[A-ZÕÄÖÜa-zõäöü0-9]+$/,
                  message: 'Username can only include letters and numbers.',
                },
              ]}
            >
              <Input />
            </Form.Item>
            <Form.Item 
              label="E-mail"
              name="email"
              rules={[
                {
                  type: 'email',
                  message: 'Please input a correctly formatted e-mail address!'
                },
                {
                  required: true,
                  message: 'Please input your e-mail!',
                },
              ]}
            >
              <Input />
            </Form.Item>

            <Form.Item 
              label="Password"
              name="password"
              rules={[
                {
                  required: true,
                  message: 'Please choose a password!',
                },
                {
                  min: 8,
                  message: 'Password must be at least 8 characters long'
                },
              ]}
              hasFeedback
            >
              <Input.Password />
            </Form.Item>

            <Form.Item 
              label="Confirm Password"
              name="confirm"
              dependencies={['password']}
              hasFeedback
              rules={[
                {
                  required: true,
                  message: 'Please confirm your password!',
                },
                ({ getFieldValue }) => ({
                  validator(_, value) {
                    if (!value || getFieldValue('password') === value) {
                      return Promise.resolve();
                    }
                    return Promise.reject(new Error('The passwords do not match!'));
                  },
                }),
              ]}
            >
              <Input.Password />
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
    { state.auth.token &&
      (
        <Title style={{textAlign: 'center'}}>Welcome, {state.user}!</Title>
      )
    }
    </>
  )
}

export default Register;