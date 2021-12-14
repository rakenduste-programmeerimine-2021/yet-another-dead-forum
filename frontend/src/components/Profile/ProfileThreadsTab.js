import {List, Space} from 'antd';
import {ClockCircleOutlined, MessageOutlined} from "@ant-design/icons";
import React from "react";
import {Link} from "react-router-dom";
import {format} from "date-fns";

const ProfileThreadsTab = ({threads}) => {

    const IconText = ({ icon, text }) => (
        <Space>
            {React.createElement(icon)}
            {text}
        </Space>
    )

    return (
        <List
            itemLayout="vertical"
            size="large"
            pagination={{
                onChange: page => {
                    console.log(page);
                },
                pageSize: 5,
            }}
            dataSource={threads}
            renderItem={item => (
                <List.Item
                    key={item.title}
                    actions={[
                        <IconText icon={MessageOutlined} text={item.posts.length} key="list-vertical-message" />,
                        item.createdAt && <IconText icon={ClockCircleOutlined} text={format(new Date(item.createdAt), 'dd. MMM yyyy | HH:mm')}/>
                    ]}
                >
                    <List.Item.Meta
                        title={<Link to={`/thread/${item.id}`}>{item.title}</Link>}
                        description={`${item.text.slice(0, 250)}${item.text.length >=250 ? '...' : ''}`}
                    />
                </List.Item>
            )}
        />
    )
}

export default ProfileThreadsTab