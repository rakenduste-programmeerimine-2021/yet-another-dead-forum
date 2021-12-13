import {Button} from "antd";
import { Empty } from 'antd';
import { Card } from 'antd';
import {Context} from "../../store";
import {useContext} from "react";


const ProfileAbout = ({signature, username}) => {

    const [state, dispatch] = useContext(Context);

    return (
        <Card title="Signature" style={{marginBottom:'50px'}}>
        {
            signature
            ?
                <p>{signature}</p>
            :
            <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description={"This user has no signature"}/>
        }
            {
                (state.auth.user.roles.includes('ROLE_ADMIN') || state.auth.user.username === username)
                &&
                <Button style={{float:'right'}} href={`/user/${username}/edit/about`}>Edit Signature</Button>
            }
        </Card>
    )
}

export default ProfileAbout