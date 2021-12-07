import { format } from 'date-fns';
import { List, Typography } from 'antd';

const PostItem = ({ post }) => {
  const { Text } = Typography;

  return(
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
  )
};

export default PostItem;