import { useContext, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Space, Typography } from 'antd';
import { Context } from '../../store';
import { updateSingleThread, editThread, resetSingleThread } from '../../store/actions';

const ThreadEdit = () => {
  const [title, setTitle] = useState('');
  const [text, setText] = useState('');
  const [error, setError] = useState('');
  const [state, dispatch] = useContext(Context);
  const navigate = useNavigate();
  const params = useParams();  
  
  useEffect(() => {
    fetch('http://localhost:8080/api/thread/' + params.id).then(res => {
      return res.json();
    }).then(async (data) => {
      await dispatch(updateSingleThread(data))
    })
  }, [])

  const { TextArea } = Input;
  const { Title, Text } = Typography;

  const handleSubmit = async () => {
    const threadData = {
      ...state.threads.singleThread,
      author: {
        ...state.threads.singleThread.author,
        username: state.auth.user.username,
      },
      title: title ? title : state.threads.singleThread.title,
      text: text ? text : state.threads.singleThread.text,
      updatedAt: new Date()
    }

    const res = await fetch('http://localhost:8080/api/thread/edit', {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(threadData),
    })
    
    const returnData = await res.json();

    if(res.ok) {
      dispatch(editThread(returnData));
      navigate('/thread/' + params.id, { replace: true });
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

  const cancelEdit = () => {
    dispatch(resetSingleThread());
    navigate(-1);
  }

  const handleError = (err) => {
    console.log(err)
  }

  return(
    <>
    { (Object.keys(state.threads.singleThread).length != 0 && state.auth.token &&
        state.auth.user.username === state.threads.singleThread.author.username) &&
      (
        <>
          <Title level={5} style={{textAlign: 'center'}}>Edit thread</Title>
          <Form
            name="basic"
            style={{maxWidth: '50%', margin: 'auto'}}
            initialValues={{ remember: true }}
            onFinish={handleSubmit}
            onFinishFailed={handleError}
            autoComplete="off"
            initialValues={{
              'title': state.threads.singleThread.title,
              'text': state.threads.singleThread.text
            }}
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
              name="text"
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
                onChange={e => setText(e.target.value)}
              />
            </Form.Item>
            { error && <Text style={{whiteSpace: 'pre-wrap'}} type="danger">{ error }</Text> }

            <Form.Item style={{textAlign: 'center'}}>
              <Space>
                <Button type="primary" htmlType="submit">
                  Submit
                </Button>
                {/* Credit: https://stackoverflow.com/questions/65948671/how-to-go-back-to-previous-route-in-react-router-dom-v6 */}
                <Button danger onClick={cancelEdit}>Cancel</Button>
              </Space>
            </Form.Item>

          </Form>
        </>
      )
    }
    { !state.auth.token &&
      (
        <Title style={{textAlign: 'center'}}>Please log in to edit this thread</Title>
      )
    }
  </>
  )
};

export default ThreadEdit;