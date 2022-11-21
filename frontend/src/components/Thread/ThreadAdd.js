import { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Input, Button, Space, Typography } from 'antd';
import { Context } from '../../store';
import { addThread } from '../../store/actions';

const ThreadAdd = () => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [error, setError] = useState('');
  const [state, dispatch] = useContext(Context);
  const navigate = useNavigate();

  const { TextArea } = Input;
  const { Title, Text } = Typography;

  const handleSubmit = async () => {
    const threadData = {
      username: state.auth.user.username,
      title,
      content
    }

    const res = await fetch(`${process.env.REACT_APP_SITE_URL}/api/thread/add`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + state.auth.token
      },
      body: JSON.stringify(threadData),
    })
    
    const returnData = await res.json();

    if(res.ok) {
      dispatch(addThread(returnData));
      navigate('/', { replace: true });
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
    { state.auth.token &&
      (
        <>
          <Title level={5} style={{textAlign: 'center'}}>Create a new thread</Title>
          <Form
            name="basic"
            style={{maxWidth: '50%', margin: 'auto'}}
            initialValues={{ remember: true }}
            onFinish={handleSubmit}
            onFinishFailed={handleError}
            autoComplete="off"
          >
            <Form.Item 
              label="Topic"
              name="title"
              rules={[
                {
                  required: true,
                  message: 'Please let people know what your post is about!',
                },
              ]}
            >
              <TextArea
                autoSize={{ minRows: 1, maxRows: 3 }}
                allowClear
                onChange={e => setTitle(e.target.value)}
              />
            </Form.Item>

            <Form.Item 
              label="Post"
              name="content"
              rules={[
                {
                  required: true,
                  message: 'Please write what you\'d like to say on the matter!',
                },
              ]}
            >
              <TextArea
                autoSize={{ minRows: 5, maxRows: 15 }}
                allowClear
                onChange={e => setContent(e.target.value)}
              />
            </Form.Item>
            { error && <Text style={{whiteSpace: 'pre-wrap'}} type="danger">{ error }</Text> }

            <Form.Item style={{textAlign: 'center'}}>
              <Space>
                <Button type="primary" htmlType="submit">
                  Submit
                </Button>
                {/* Credit: https://stackoverflow.com/questions/65948671/how-to-go-back-to-previous-route-in-react-router-dom-v6 */}
                <Button danger onClick={() => navigate(-1)}>Cancel</Button>
              </Space>
            </Form.Item>

          </Form>
        </>
      )
    }
    { !state.auth.token &&
      (
        <Title style={{textAlign: 'center'}}>Please log in to create a thread</Title>
      )
    }
  </>
  )
};

export default ThreadAdd;