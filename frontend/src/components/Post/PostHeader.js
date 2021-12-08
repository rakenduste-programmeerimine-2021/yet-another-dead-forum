import { useContext } from 'react';
import { Link } from 'react-router-dom';
import { format } from 'date-fns';
import { Typography } from 'antd';
import { Context } from '../../store';

const PostHeader = ({ data }) => {
  const [state, dispatch] = useContext(Context);
  const { Text, Title } = Typography;
  const created = new Date(data.createdAt)
  const updated = new Date(data.updatedAt)
  return(
    <div style={{display: 'flex'}}>
      <div style={{display: 'flex', flexDirection: 'column'}}>
        <span>{data.author.username}</span>
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
      {state.auth.token && (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.roles.includes('ROLE_MODERATOR') || parseInt(state.auth.user.id, 10) === data.author.id) &&
        <>
          <span className="delete">DELETE</span>
          <Link className="edit" to={"/thread/edit/" + data.id}>EDIT</Link>
        </>
      }
    </div>
  )
};

export default PostHeader;