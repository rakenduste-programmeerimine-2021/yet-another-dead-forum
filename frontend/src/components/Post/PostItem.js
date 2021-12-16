import {useContext} from 'react';
import {Link} from 'react-router-dom';
import {format} from 'date-fns';
import {Divider, List, Space, Typography} from 'antd';
import {Context} from '../../store';
import {deletePost} from '../../store/actions';
import {ClockCircleOutlined} from "@ant-design/icons";

const PostItem = ({post}) => {
  const [state, dispatch] = useContext(Context);
  const {Text} = Typography;

  const created = new Date(post.createdAt)
  const updated = new Date(post.updatedAt)

  const postDelete = async (data) => {
    if (window.confirm("Are you sure you want to delete this post?")) {
      const res = await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/post/delete/` + data.id, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + state.auth.token
        },
        body: JSON.stringify({id: data.id}),
      })

      if (res.ok) {
        dispatch(deletePost(data))
      } else {
        let errors = ''
        if (res.json().error) {
          errors = res.json().error
        } else {
          for (let i = 0; i < res.json().msg.length; i++) {
            errors += res.json().msg[i].param[0].toUpperCase() + res.json().msg[i].param.slice(1) + ' ' + res.json().msg[i].msg + '\n'
          }
        }
        console.log(errors)
      }
    }
  }

  return (
    <List.Item>
      <div className={'content-list'} style={{display: 'flex', gap: '15px'}}>
        <div style={{display: 'flex', flexDirection: 'column', flex: '0 0 10em'}}>
          <span><Link to={"/user/" + post.author.username}>{post.author.displayName}</Link></span>
          <span>Posts: {post.author.threadAmount + post.author.postAmount}</span>
          <span>Join date: {format(new Date(post.author.createdAt), 'dd. MMM yyyy')}</span>
          {post.author.roles.map((role, i) => (
            ((role.id === 1 && post.author.roles.length === 1) || (role.id !== 1 && post.author.roles.length > 1)) &&
            <div key={i} style={{backgroundColor: role.bodyCss, textAlign: 'center', marginBottom: '5px'}}>
              <Text style={{color: role.textCss, marginLeft: '5px'}}>{role.displayName}</Text>
            </div>
          ))}
        </div>
        <div style={{display: 'flex', flexDirection: 'column', justifyContent: 'space-between'}}>
          <Text style={{marginBottom: '20px'}}>{post.text}</Text>
          {(post.author.signature && post.author.signature.length) &&
          <div><Divider dashed/><p>{post.author.signature}</p><Divider dashed/></div>}
          <Space size={"large"} style={{display: 'flex', width: '100%'}}>
            {post.createdAt && <Text style={{fontSize: '11px'}}
                                     type='secondary'><ClockCircleOutlined/> {format(new Date(post.createdAt), 'dd. MMM yyyy')}
            </Text>}
            {
              Math.round(created.getTime() / 1000) < Math.round(updated.getTime() / 1000) &&
              <Text type="secondary" style={{fontSize: '11px', fontStyle: 'italic'}}>Edited</Text>
            }
            {state.auth.token &&
            (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR')) &&
            <span className="delete" onClick={() => postDelete(post)}>DELETE</span>
            }
            {state.auth.token &&
            (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR') || parseInt(state.auth.user.id, 10) === post.author.id) &&
            <Link className="edit" to={"/post/edit/" + post.id}> EDIT</Link>
            }
          </Space>
        </div>
      </div>
    </List.Item>
  )
};

export default PostItem;