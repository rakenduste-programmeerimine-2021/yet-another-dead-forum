import { useContext, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { format } from 'date-fns';
import { Button, ConfigProvider, Empty, Form, Input, List, Typography } from 'antd';
import { Context } from '../store';
import { updatePosts } from '../store/actions';

const Posts = () => {
  const [state, dispatch] = useContext(Context);
  const { Text, Title } = Typography;
  const params = useParams();  
  
  useEffect(() => {
    fetch('http://localhost:8080/api/thread/' + params.id).then(res => {
      return res.json();
    }).then(async (data) => {
      await dispatch(updatePosts(data))
    })
  }, [])

  const noPosts = () => (
    <Empty
      style={{ textAlign: 'center' }}
      image={Empty.PRESENTED_IMAGE_SIMPLE}
      description={
        <p>No posts found</p>
      }
    />
  )

  return(
    <>
    {state.posts.data.length != 0 &&
    <>
    <ConfigProvider renderEmpty={state.posts.data.posts.length <= 0 && noPosts}>
      <List 
        itemLayout="horizontal"
        dataSource={state.posts.data.posts}
        pagination={{
          pageSize: 5,
        }}
        header={
          <div style={{display: 'flex'}}>
            <div style={{display: 'flex', flexDirection: 'column'}}>
              <span>{state.posts.data.author.username}</span>
              <span>Posts: </span>
              <span>Join date: {format(new Date(state.posts.data.author.createdAt), 'dd. MMM yyyy')}</span>
              <span>
                {state.posts.data.author.roles.map((role) => role.name + ' ').includes('ROLE_ADMIN')
                  ? <Text>Admin</Text>
                  : state.posts.data.author.roles.map((role) => role.name + ' ').includes('ROLE_MODERATOR')
                    ? <Text>Moderator</Text>
                    : state.posts.data.author.roles.map((role) => role.name + ' ').includes('ROLE_PREMIUM')
                      ? <Text>Premium Member</Text>
                      : <Text>Member</Text>
                }
              </span>
            </div>
            <div style={{display: 'flex', flexDirection: 'column'}}>
              <Title level={5}>{state.posts.data.title}</Title>
              <Text>{state.posts.data.text}</Text>
            </div>
          </div>
        }
        renderItem={post => (
          <List.Item>
            <div style={{display: 'flex'}}>
              <div style={{display: 'flex', flexDirection: 'column'}}>
                <span>{post.author.username}</span>
                <span>Posts: </span>
                <span>Join date: {format(new Date(post.author.createdAt), 'dd. MMM yyyy')}</span>
                <span>
                  {post.author.roles.map((role) => role.name).includes('ROLE_ADMIN')
                    ? <Text>Admin</Text>
                    : post.author.roles.map((role) => role.name).includes('ROLE_MODERATOR')
                      ? <Text>Moderator</Text>
                      : post.author.roles.map((role) => role.name).includes('ROLE_PREMIUM')
                        ? <Text>Premium Member</Text>
                        : <Text>Member</Text>
                  }
                </span>
              </div>
              <div style={{display: 'flex', flexDirection: 'column'}}>
                <Text>{post.text}</Text>
              </div>
            </div>
          </List.Item>
        )}
        />
        </ConfigProvider>
        {
          state.auth.token
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
    }
    </>
  )
};

export default Posts;