import {Card} from 'antd';
import {useState} from "react";
import ProfilePostsTab from "./ProfilePostsTab";
import ProfileThreadsTab from "./ProfileThreadsTab";

const tabList = [
  {
    key: 'threads',
    tab: 'Threads',
  },
  {
    key: 'posts',
    tab: 'Posts',
  },
];

const ProfileUserContent = ({posts, threads, displayName}) => {
  const [activeTabKey, setActiveTabKey] = useState('tab1');
  const contentList = {
    threads: <ProfileThreadsTab threads={threads.sort((a, b) => b.id - a.id)}/>,
    posts: <ProfilePostsTab posts={posts.sort((a, b) => b.id - a.id)}/>
  };

  const onTabChange = key => {
    setActiveTabKey(key);
  };


  return (
    <>
      <Card
        style={{width: '100%'}}
        title={`Content by ${displayName}`}
        tabList={tabList}
        activeTabKey={activeTabKey}
        onTabChange={key => {
          onTabChange(key);
        }}
      >
        {contentList[activeTabKey]}
      </Card>
    </>
  );
};

export default ProfileUserContent