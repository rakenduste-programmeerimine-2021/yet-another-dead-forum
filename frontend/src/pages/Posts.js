import { useContext, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { ConfigProvider, Empty, List } from 'antd';
import { Context } from '../store';
import { updatePosts } from '../store/actions';
import PostHeader from '../components/Post/PostHeader';
import PostItem from '../components/Post/PostItem';
import PostAdd from '../components/Post/PostAdd';

const Posts = () => {
  const [state, dispatch] = useContext(Context);
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
        <p>No posts found - be the first to reply!</p>
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
                <PostHeader data={state.posts.data} />
              }
              renderItem={post => (
                <PostItem post={post} />
              )}
              />
          </ConfigProvider>
          <PostAdd />
        </>
      }
    </>
  )
};

export default Posts;