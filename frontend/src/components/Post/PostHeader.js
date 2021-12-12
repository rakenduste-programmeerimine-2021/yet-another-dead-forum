import { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import { Typography } from 'antd';
import { Context } from '../../store';
import { deleteThread } from '../../store/actions';

const PostHeader = ({ data }) => {
  const [error, setError] = useState('');
  const [state, dispatch] = useContext(Context);
  const { Text, Title } = Typography;
  const navigate = useNavigate();

  const created = new Date(data.createdAt)
  const updated = new Date(data.updatedAt)

  const threadDelete = async (data) => {
    if (window.confirm("Are you sure you want to deltete the thread\n" + data.title + "?")) {
      const res = await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/thread/delete/` + data.id, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + state.auth.token
        },
        body: JSON.stringify({ id: data.id }),
      })

      if(res.ok) {
        dispatch(deleteThread(data))
        navigate('/', { replace: true });
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
      <div style={{display: 'flex'}}>
        <div style={{display: 'flex', flexDirection: 'column'}}>
          <span><Link to={"/user/" + data.author.username}>{data.author.displayName}</Link></span>
          <span>Posts: </span>
          <span>Join date: {format(new Date(data.author.createdAt), 'dd. MMM yyyy')}</span>
          <span>
            {data.author.roles.map((role) => role.name + ' ').includes('ROLE_ADMIN')
              ? <Text>Admin</Text>
              : data.author.roles.map((role) => role.name + ' ').includes('ROLE_MODERATOR')
                ? <Text>Moderator</Text>
                : data.author.roles.map((role) => role.name + ' ').includes('ROLE_PREMIUM')
                  ? <Text>Premium Member</Text>
                  : <Text>Member</Text>
            }
          </span>
        </div>
        <div style={{display: 'flex', flexDirection: 'column'}}>
          <Title level={5}>{data.title}</Title>
          <Text>{data.text}</Text>
          {
            created.getTime() < updated.getTime() &&
            <Text style={{ fontSize: '11px', fontStyle: 'italic' }}>Edited</Text>
          }
        </div>
        {state.auth.token &&
          <>
          {
          (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR')) &&
            <span className="delete" onClick={() => threadDelete(data)}>DELETE</span>
          }
          {
            (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR') || parseInt(state.auth.user.id, 10) === data.author.id) &&
            <Link className="edit" to={"/thread/edit/" + data.id}> EDIT</Link>
          }
          </>
        }
      </div>
    </>
  )
};

export default PostHeader;