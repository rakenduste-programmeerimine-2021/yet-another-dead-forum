import { Link } from 'react-router-dom';
import { format } from 'date-fns';
import { List, Typography } from 'antd';

const PostItem = ({ post }) => {
  const { Text } = Typography;

  return(
    <List.Item>
      <div style={{display: 'flex'}}>
        <div style={{display: 'flex', flexDirection: 'column'}}>
        <span><Link to={"/user/" + post.author.username}>{post.author.displayName}</Link></span>
          <span>Posts: </span>
          <span>Join date: {format(new Date(post.author.createdAt), 'dd. MMM yyyy')}</span>
          {post.author.roles.map((role, i) => (
            <div key={i} style={{backgroundColor: role.bodyCss}}>
              <Text style={{color: role.textCss, marginLeft: '5px'}}>{role.displayName}</Text>
            </div>
          ))}
        </div>
        <div style={{display: 'flex', flexDirection: 'column' }}>
          <Text>{post.text}</Text>
        </div>
      </div>
    </List.Item>
  )
};

export default PostItem;