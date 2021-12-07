import { useContext } from 'react';
import { Button, Form, Input, Typography } from 'antd';
import { Context } from '../../store';

const PostAdd = () => {
  const [state, dispatch] = useContext(Context);
  const { Text } = Typography;

  return(
    <>
      {state.auth.token
        ?
          <Form
            name="post"
            style={{maxWidth: '50%', margin: 'auto'}}
            initialValues={{ remember: true }}
            // onFinish={handleSubmit}
            // onFinishFailed={handleError}
            autoComplete="off"
          >
            <Form.Item
              name="post"
            >
              <Input.TextArea />
            </Form.Item>
            <Form.Item style={{textAlign: 'center'}}>
              <Button type="primary" htmlType="submit">
                Submit
              </Button>
            </Form.Item>
          </Form>
        :
          <Text>You must log in to reply</Text>
      }
    </>
  )
};

export default PostAdd;