import { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { format } from 'date-fns';
import {Divider, Space, Typography} from 'antd';
import { Context } from '../../store';
import { deleteThread } from '../../store/actions';
import {ClockCircleOutlined} from "@ant-design/icons";

const PostHeader = ({ data }) => {
  const [error, setError] = useState('');
  const [state, dispatch] = useContext(Context);
  const { Text, Title } = Typography;
  const navigate = useNavigate();

  const created = new Date(data.createdAt)
  const updated = new Date(data.updatedAt)

  const threadDelete = async (data) => {
    if (window.confirm("Are you sure you want to delete the thread\n" + data.title + "?")) {
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
      <div style={{display: 'flex', gap: '15px'}}>
        <div style={{display: 'inline-flex', flexDirection: 'column', flex: '0 0 10em'}}>
          <span><Link to={"/user/" + data.author.username}>{data.author.displayName}</Link></span>
          <span>Posts: {data.author.threadAmount + data.author.postAmount}</span>
          <span>Join date: {format(new Date(data.author.createdAt), 'dd. MMM yyyy')}</span>
          {data.author.roles.map((role, i) => (
            ((role.id === 1 && data.author.roles.length === 1 ) || (role.id !== 1 && data.author.roles.length > 1)) &&
            <div key={i} style={{backgroundColor: role.bodyCss, textAlign: 'center', marginBottom:'5px'}}>
              <Text style={{color: role.textCss, marginLeft: '5px'}}>{role.displayName}</Text>
            </div>
          ))}
        </div>
        <div style={{display: 'flex', flexDirection: 'column', justifyContent:'space-between'}}>
          <Title level={5}>{data.title}</Title>
          <Text>{data.text}</Text>
          {(data.author.signature && data.author.signature.length) && <div><Divider dashed/><p>{data.author.signature}</p><Divider dashed/></div>}
          <Space size="large">
            <Space id="test"  size={"large"}>
              {data.createdAt && <Text type='secondary' style={{ fontSize: '11px'}} ><ClockCircleOutlined /> {format(new Date(data.createdAt), 'dd. MMM yyyy')}</Text>}
            </Space>
          {
            Math.round(created.getTime() / 1000) < Math.round(updated.getTime() / 1000) &&
            <Text type="secondary" style={{ fontSize: '11px', fontStyle: 'italic' }}>Edited</Text>
          }
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
          </Space>
        </div>
      </div>
    </>
  )
}

export default PostHeader;