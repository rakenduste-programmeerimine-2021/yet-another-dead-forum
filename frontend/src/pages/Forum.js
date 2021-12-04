import { useContext, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button, List, Typography } from 'antd';
import { Context } from '../store';
import { updateThreads } from '../store/actions';

const Forum = () => {
  const [state, dispatch] = useContext(Context);
  const { Text, Title } = Typography;
  
  useEffect(() => {
    fetch('http://localhost:8080/api/threads').then(res => {
      return res.json();
    }).then(async (data) => {
      await dispatch(updateThreads(data))
      console.log(state.auth)
    })
  }, [])

  return(
    <>
      {
        state.auth.token
          ?
          <Button>Create Thread</Button>
          :
          <Text>You must log in to create a thread</Text>
      }
      <List itemLayout="horizontal" dataSource={state.threads.data} renderItem={thread => (
        <List.Item>
          <div style={{ maxWidth: '100%' }}>
            <div><Link to={"/user/" + thread.author.username}>{thread.author.username}</Link>
              {thread.createdAt &&
                <span> Submitted {thread.createdAt}</span>
              }
            </div>
            <Link to={"/thread/" + thread.id}><Title level={5} ellipsis>{thread.title}</Title></Link>
            <div>{thread.posts.length} Posts
              {thread.posts.length > 0 && 
                <span> Last post by&nbsp;
                  <Link to={"/user/" + thread.posts[thread.posts.length - 1].author.username}>
                    {thread.posts[thread.posts.length - 1].author.username}
                  </Link>
                {thread.posts[thread.posts.length - 1].createdAt &&
                  <span> at {thread.posts[thread.posts.length - 1].createdAt}</span>
                }
                </span>
              }
              {/* {state.auth.user.roles.includes('ROLE_MODERATOR') &&
                <span>DELETE EDIT</span>
              } */}
            </div>
          </div>
        </List.Item>
      )} />
    </>
  )
};

export default Forum;