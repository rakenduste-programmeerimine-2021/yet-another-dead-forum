import { useContext, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Form, Input, Typography } from 'antd';
import { Context } from '../../store';
import { addPost } from '../../store/actions';
import {InfoCircleOutlined} from "@ant-design/icons";

const PostAdd = () => {
  const [error, setError] = useState('');
  const [state, dispatch] = useContext(Context);
  const params = useParams();
  const navigate = useNavigate();
  const [form] = Form.useForm();
  const { Text } = Typography;

  const handleSubmit = async ({ text }) => {
    const post = {
      username: state.auth.user.username,
      text,
      threadId: params.id
    }

    const res = await fetch(`${process.env.REACT_APP_SITE_URL}/api/post/add`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + state.auth.token
      },
        body: JSON.stringify(post),
    })

    const returnData = await res.json()

    if (res.ok) {
      form.resetFields();
      dispatch(addPost(returnData))
      navigate('/thread/' + params.id, { replace: true });
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

  return(
    <>
      {state.auth.token
        ?
          <Form
            form={form}
            name="post"
            style={{maxWidth: '50%', margin: 'auto', marginTop: '50px'}}
            initialValues={{ remember: true }}
            onFinish={handleSubmit}
            // onFinishFailed={handleError}
            autoComplete="off"
          >
            <Form.Item
              name="text"
            >
              <Input.TextArea />
            </Form.Item>
            { error && <Text style={{whiteSpace: 'pre-wrap'}} type="danger">{ error }</Text> }
            <Form.Item style={{textAlign: 'center'}}>
              <Button type="primary" htmlType="submit">
                Submit
              </Button>
            </Form.Item>
          </Form>
        :
          <div style={{marginTop: '50px', textAlign:'center'}}>
            <Text><InfoCircleOutlined /> You must log in to reply</Text>
          </div>
      }
    </>
  )
};

export default PostAdd;