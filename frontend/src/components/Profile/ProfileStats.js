import { Descriptions} from 'antd';
import {format} from "date-fns";
import { Card } from 'antd';

const ProfileStats = ({stats}) => {

    function displayRoles(roles)  {
        return roles.map(r => <span
            key={r['id']}
            style={{textAlign:'center', backgroundColor: r['bodyCss'], color: r['textCss'], padding:'5px 15px', margin:'5px'}}
            >{r['displayName']}</span>)
    }

    return (
        <Card title="Stats" style={{marginBottom:'50px'}}>
            <Descriptions layout="vertical" bordered>
                <Descriptions.Item labelStyle={{fontWeight: 'bold'}} label="Join date">{format(new Date(stats.createdAt), 'dd. MMM yyyy | HH:mm')}</Descriptions.Item>
                <Descriptions.Item labelStyle={{fontWeight: 'bold'}} label="Threads">{stats.threadAmount}</Descriptions.Item>
                <Descriptions.Item labelStyle={{fontWeight: 'bold'}} label="Posts">{stats.postAmount}</Descriptions.Item>
                <Descriptions.Item labelStyle={{fontWeight: 'bold'}} label="Profile Visits">{stats.visits}</Descriptions.Item>
                <Descriptions.Item labelStyle={{fontWeight: 'bold'}} label="Roles">{displayRoles(stats.roles)}</Descriptions.Item>
            </Descriptions>
        </Card>
    )
}

export default ProfileStats