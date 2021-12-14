import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { format } from 'date-fns';
import { List, Typography } from 'antd';
import { Context } from '../../store';
import { deletePost } from '../../store/actions';

const PostItem = ({ post }) => {
  const [state, dispatch] = useContext(Context);
  const { Text } = Typography;

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
        body: JSON.stringify({ id: data.id }),
      })

      if(res.ok) {
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

  return(
    <List.Item>
      <div style={{display: 'flex', gap: '15px'}}>
        <div style={{display: 'flex', flexDirection: 'column'}}>
        <span><Link to={"/user/" + post.author.username}>{post.author.displayName}</Link></span>
          <span>Posts: </span>
          <span>Join date: {format(new Date(post.author.createdAt), 'dd. MMM yyyy')}</span>
          {post.author.roles.map((role, i) => (
            <div key={i} style={{backgroundColor: role.bodyCss, textAlign: 'center'}}>
              <Text style={{color: role.textCss, marginLeft: '5px'}}>{role.displayName}</Text>
            </div>
          ))}
        </div>
        <div style={{display: 'flex', flexDirection: 'column' }}>
          <Text>{post.text}</Text>
          {
            Math.round(created.getTime() / 1000) < Math.round(updated.getTime() / 1000) &&
            <Text style={{ fontSize: '11px', fontStyle: 'italic' }}>Edited</Text>
          }
          {state.auth.token &&
            <>
            {
            (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR')) &&
              <span className="delete" onClick={() => postDelete(post)}>DELETE</span>
            }
            {
              (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR') || parseInt(state.auth.user.id, 10) === post.author.id) &&
              <Link className="edit" to={"/post/edit/" + post.id}> EDIT</Link>
            }
            </>
          }
        </div>
      </div>
    </List.Item>
  )
};

export default PostItem;