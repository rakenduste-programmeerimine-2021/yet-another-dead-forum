import React, { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { format } from 'date-fns';
import {Button, List, Space, Typography} from 'antd';
import { Context } from '../store';
import { deleteThread, updateThreads, resetSingleThread } from '../store/actions';
import './Forum.css'
import {ClockCircleOutlined, CommentOutlined, UsergroupAddOutlined, UserOutlined} from "@ant-design/icons";

const Forum = () => {
  const [error, setError] = useState('');
  const [state, dispatch] = useContext(Context);
  const { Text, Title } = Typography;
  
  useEffect(() => {
    fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/threads`).then(res => {
      return res.json();
    }).then(async (data) => {
      await dispatch(updateThreads(data))
      dispatch(resetSingleThread())
    })
  }, [])

  const ThreadMetaTop = ({ author, date }) => (
      <Space size={"large"} style={{marginBottom:'15px'}} >
        <Space size={"small"}>
          <UserOutlined />
          <Link to={"/user/" + author}>
            {author}
          </Link>
        </Space>
        {date && <div><ClockCircleOutlined /> {format(new Date(date), 'dd. MMM yyyy')}</div>}
      </Space>
  )

  const ThreadMetaBottom = ({ posts }) => (
      <Space size='large' style={{marginTop:'15px'}}>
        <Space size='small'>
          <CommentOutlined />{posts.length}
        </Space>
        {(posts.length > 0) &&
            <Space size = 'large'>
              <Text type="secondary"><UsergroupAddOutlined style={{color:"rgba(0, 0, 0, 0.85)"}}/> Last post by{' '}
                <Link to={"/user/" + posts[posts.length - 1].author.username}>
                    {posts[posts.length - 1].author.displayName}
                </Link>
              </Text>
              <Text type="secondary"><ClockCircleOutlined style={{color:"rgba(0, 0, 0, 0.85)"}} /> {format(new Date(posts[posts.length - 1].createdAt), 'dd. MMM yyyy | HH:mm')}</Text>
            </Space>
        }
      </Space>
  )

  const threadDelete = async (thread) => {
    if (window.confirm("Are you sure you want to deltete the thread\n" + thread.title + "?")) {
      const res = await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/thread/delete/` + thread.id, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + state.auth.token
        },
        body: JSON.stringify({ id: thread.id }),
      })

      if(res.ok) {
        dispatch(deleteThread(thread))
      } else {
        let errors = ''
          if (res.json().error) {
            errors = res.json().error
          } else {
            for (let i = 0; i < res.json().msg.length; i++) {
              errors += res.json().msg[i].param[0].toUpperCase() + res.json().msg[i].param.slice(1) + ' ' + res.json().msg[i].msg + '\n'
            }
          }
          setError(errors)
      }
    }
  }

  return(
    <>
      { error && <Text style={{whiteSpace: 'pre-wrap'}} type="danger">{ error }</Text> }
      {
        state.auth.token
          ?
          <Button style={{marginBottom:'20px'}} href='/thread/add'>Create Thread</Button>
          :
          <Text>You must log in to create a thread</Text>
      }
      <List
        itemLayout="horizontal"
        dataSource={state.threads.data}
        pagination={{
          pageSize: 9,
        }}
        renderItem={thread => (
          <List.Item>
            <div style={{ maxWidth: '100%' }}>
                <ThreadMetaTop date={thread.createdAt} author={thread.author.displayName} />
              <Link to={"/thread/" + thread.id}><Title level={5} ellipsis>{thread.title}</Title></Link>
              <div>
                <ThreadMetaBottom posts={thread.posts}/>
                {state.auth.token &&
                  <span className="edit-delete">
                  {
                    (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR')) &&
                    <span className="delete" onClick={() => threadDelete(thread)}>DELETE</span>
                  }
                  {
                    (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR') || parseInt(state.auth.user.id, 10) === thread.author.id) &&
                    <Link className="edit" to={"/thread/edit/" + thread.id}>EDIT</Link>
                  }
                  </span>
                }
              </div>
            </div>
          </List.Item>
      )} />
    </>
  )
};

export default Forum;