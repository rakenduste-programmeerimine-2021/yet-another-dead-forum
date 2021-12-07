import { format } from 'date-fns';
import { Typography } from 'antd';

const PostHeader = ({ data }) => {
  const { Text, Title } = Typography;
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
      </div>
    </div>
  )
};

export default PostHeader;