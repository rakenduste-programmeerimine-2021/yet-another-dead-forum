import { useContext, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Space, Typography } from 'antd';
import { Context } from '../../store';
import { updateSinglePost, editPost, resetSinglePost } from '../../store/actions';

const PostEdit = () => {
  const [text, setText] = useState('');
  const [error, setError] = useState('');
  const [state, dispatch] = useContext(Context);
  const navigate = useNavigate();
  const params = useParams();  
  
  useEffect(() => {
    fetch(`${process.env.REACT_APP_SITE_URL}/api/post/` + params.id).then(res => {
      return res.json();
    }).then(async (data) => {
      await dispatch(updateSinglePost(data))
    })
  }, [])

  const { TextArea } = Input;
  const { Title, Text } = Typography;

  const handleSubmit = async () => {
    const postData = {
      id: params.id,
      text,
      updatedAt: new Date()
    }

    const res = await fetch(`${process.env.REACT_APP_SITE_URL}/api/post/edit`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + state.auth.token
      },
      body: JSON.stringify(postData),
    })
    
    const returnData = await res.json();

    if(res.ok) {
      dispatch(editPost(returnData));
      navigate('/thread/' + state.threads.singleThread.id, { replace: true });
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
    dispatch(resetSinglePost());
    navigate(-1);
  }

  const handleError = (err) => {
    console.log(err)
  }

  return(
    <>
    { (Object.keys(state.posts.singlePost).length != 0 && state.auth.token &&
        (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR') || 
          parseInt(state.auth.user.id) === state.threads.singleThread.author.id)) &&
      (
        <>
          <Title level={5} style={{textAlign: 'center'}}>Edit post</Title>
          <Form
            name="basic"
            style={{maxWidth: '50%', margin: 'auto'}}
            initialValues={{ remember: true }}
            onFinish={handleSubmit}
            onFinishFailed={handleError}
            autoComplete="off"
            initialValues={{
              'text': state.posts.singlePost.text
            }}
          >
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
        <Title style={{textAlign: 'center'}}>Please log in to edit this post</Title>
      )
    }
  </>
  )
};

export default PostEdit;