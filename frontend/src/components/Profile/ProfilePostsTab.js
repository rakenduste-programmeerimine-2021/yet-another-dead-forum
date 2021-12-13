import {List, Space} from 'antd';
import {format} from "date-fns";
import {ClockCircleOutlined} from "@ant-design/icons";
import React from "react";

const ProfilePostsTab = ({posts}) => {

    const IconText = ({ icon, text }) => (
        <Space>
            {React.createElement(icon)}
            {text}
        </Space>
    );

    return (
        <List
            itemLayout="vertical"
            size="large"
            pagination={{
                pageSize: 5,
            }}
            dataSource={posts}
            renderItem={item => (
                <List.Item
                    key={item.text}
                    actions={[
                        <IconText icon={ClockCircleOutlined} text={format(new Date(item.createdAt), 'dd. MMM yyyy | HH:mm')}/>
                    ]}
                >
                    <List.Item.Meta
                        title={item.text}
                    />
                </List.Item>
            )}
        />
    )
}

export default ProfilePostsTab