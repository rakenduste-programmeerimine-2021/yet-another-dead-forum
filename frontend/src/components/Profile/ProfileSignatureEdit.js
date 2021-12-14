import { useContext, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {Form, Input, Button, Space, Typography, Spin} from 'antd';
import { Context } from '../../store';

const ProfileSignatureEdit = () => {
    const [signature, setSignature] = useState('');
    const [error, setError] = useState('');
    const [state] = useContext(Context);
    const navigate = useNavigate();
    const params = useParams();

    useEffect(async () => {
        const user = await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/user/username/` + params.username).then(res => {
            return res.json()
        })
        setSignature(user.signature)
    }, [])

    const { TextArea } = Input;
    const { Title, Text } = Typography;

    const handleSubmit = async () => {
        const postData = {
            username: params.username,
            signature: signature
        }

        const res = await fetch(`${process.env.REACT_APP_SITE_URL}:8080/api/user/edit/signature`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + state.auth.token
            },
            body: JSON.stringify(postData),
        })

        const returnData = await res.json();

        if(res.ok) {
            navigate('/user/' + params.username, { replace: true });
        } else {
            let errors = ''
            if (returnData.error) {
                errors = returnData.error
            } else {
                for (let i = 0; i < returnData.msg.length; i++) {
                    errors += returnData.msg[i].param[0].toUpperCase() + returnData.msg[i].param.slice(1) + ' ' + returnData.msg[i].msg + '\n'
                }
            }
            setError(errors)
        }
    }

    const cancelEdit = () => {
        navigate(-1);
    }

    const handleError = (err) => {
        console.log(err)
    }

    if (!signature) return (<Spin style={{display: 'flex', justifyContent:'center'}} />)

    return(
        <>
            { (state.auth.token &&
                (state.auth.user.roles.includes('ROLE_ADMIN') || (state.auth.user.username) === params.username)) &&
            (
                <>
                    <Title level={5} style={{textAlign: 'center'}}>Edit your signature</Title>
                    <Form
                        name="basic"
                        style={{maxWidth: '50%', margin: 'auto'}}
                        initialValues={{ remember: true }}
                        onFinish={handleSubmit}
                        onFinishFailed={handleError}
                        autoComplete="off"
                        initialValues={{
                            'text': signature
                        }}
                    >
                        <Form.Item
                            label="Text"
                            name="text"
                            rules={[
                                {
                                    required: true,
                                    message: 'Please write what you\'d like others to know when catching a glimpse of your content!',
                                },
                            ]}
                        >
                            <TextArea
                                autoSize={{ minRows: 5, maxRows: 15 }}
                                allowClear
                                onChange={e => setSignature(e.target.value)}
                            />
                        </Form.Item>
                        { error && <Text style={{whiteSpace: 'pre-wrap'}} type="danger">{ error }</Text> }

                        <Form.Item style={{textAlign: 'center'}}>
                            <Space>
                                <Button type="primary" htmlType="submit">
                                    Submit
                                </Button>
                                {/* Credit: https://stackoverflow.com/questions/65948671/how-to-go-back-to-previous-route-in-react-router-dom-v6 */}
                                <Button danger onClick={cancelEdit}>Cancel</Button>
                            </Space>
                        </Form.Item>
                    </Form>
                </>
            )
            }
            { !state.auth.token &&
            (
                <Title style={{textAlign: 'center'}}>Please log in to edit this profile signature</Title>
            )
            }
        </>
    )
};

export default ProfileSignatureEdit;