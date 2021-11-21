import { useState } from 'react';
import { Form, Input, Button, Typography } from 'antd';

function Register() {
  const [error, setError] = useState('')

  const handleSubmit = async (values) => {
    const user = {
      username: values.userName,
      email: values.email,
      password: values.password
    }

    if (!values.userName || !values.email || !values.password || !values.confirm) {
      setError('Please fill out all of the fields')
    } else if (values.password !== values.confirm) {
      setError('Passwords do not match!')
    } else {
      const res = await fetch('http://localhost:8080/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
          body: JSON.stringify(user),
      })

      const returnData = await res.json()

      if (res.ok) {
        setError('')
        console.log("Success! User registered!")
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
  }

  const { Title, Text } = Typography;
  return(
    <>
      <Title style={{textAlign: 'center'}}>Register</Title>
      <Form
        name="basic"
        style={{maxWidth: '50%', margin: 'auto'}}
        initialValues={{ remember: true }}
        onFinish={handleSubmit}
        autoComplete="off"
      >
        <Form.Item 
          label="Username"
          name="userName"
          required
        >
          <Input />
        </Form.Item>
        <Form.Item 
          label="E-mail"
          name="email"
          required
        >
          <Input />
        </Form.Item>

        <Form.Item 
          label="Password"
          name="password"
          required
        >
          <Input.Password />
        </Form.Item>

        <Form.Item 
          label="Confirm Password"
          name="confirm"
          required
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

export default Register;