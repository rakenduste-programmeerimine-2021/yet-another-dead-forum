import { useContext, useState } from 'react';
import { Context } from '../store';
import { Typography } from 'antd';

const Forum = () => {
  const [state, dispatch] = useContext(Context);
  const { Title, Text } = Typography;
  return(
    <>
      <Title style={{textAlign: 'center'}}>Forum</Title>
      {
        state.auth.token
          ?
          <Text>Logged in</Text>
          :
          <Text>Logged out</Text>
      }
    </>
  )
};

export default Forum;