import {ClearOutlined} from '@ant-design/icons';
import styles from './dialog-message-actions.module.scss';
import {Select} from 'antd'
import BreakIcon from "../../icons/break.svg";
import {userChatStore} from '@/app/store/chat-store';
import {GptVersion} from '../../constants'
import {SessionConfig} from "@/types/chat";
import { CSSProperties, useRef, useState } from 'react';

export function Action(props: {
    icon: JSX.Element;
    onClick?: () => void;
    styles?: CSSProperties
}) {
    const {styles: sty} = props
    return <div className={styles['chat-input-action']}  onClick={props.onClick}>
        <div className={styles["icon"]}>
            {props.icon}
        </div>
    </div>
}
export function ChatAction(props: {
    text?: string;
    icon: JSX.Element;
    onClick: () => void;
}) {
    const iconRef = useRef<HTMLDivElement>(null);
    const textRef = useRef<HTMLDivElement>(null);
    const [width, setWidth] = useState({
        full: 16,
        icon: 16,
    });

    function updateWidth() {
        if (!iconRef.current || !textRef.current) return;
        const getWidth = (dom: HTMLDivElement) => dom.getBoundingClientRect().width;
        const textWidth = getWidth(textRef.current);
        const iconWidth = getWidth(iconRef.current);
        setWidth({
            full: textWidth + iconWidth,
            icon: iconWidth,
        });
    }

    return (
        <div
            className={`${styles["chat-input-action"]} clickable`}
            onClick={() => {
                props.onClick();
                setTimeout(updateWidth, 1);
            }}
            onMouseEnter={updateWidth}
            onTouchStart={updateWidth}
            style={
                {
                    "--icon-width": `${width.icon}px`,
                    "--full-width": `${width.full}px`,
                } as React.CSSProperties
            }
        >
            <div ref={iconRef} className={styles["icon"]}>
                {props.icon}
            </div>
            <div className={styles["text"]} ref={textRef}>
                {props.text}
            </div>
        </div>
    );
}
export default function DialogMessagesActions(props: {
    config: SessionConfig
}){
    const chatStore = userChatStore();
    const {config} = props
    return <div className={styles['chat-input-actions']}>
        <Select
            value={config?.gptVersion??GptVersion.GLM_4v}
            style={{ width: 160 }}
            options={[
                { value: GptVersion.CharacterGLM, label: 'charglm-3' },
                { value: GptVersion.CogView, label: 'cogview-3' },
                { value: GptVersion.GLM_3_Turbo, label: 'glm-3-turbo' },
                { value: GptVersion.GLM_4, label: 'glm-4' },
                { value: GptVersion.GLM_4v, label: 'glm-4v' },
                
            ]}
            onChange={(value) => {
                chatStore.updateCurrentSession((session) => {
                    session.config = {
                        ...session.config,
                        gptVersion: value
                    }
                });
            }}
        />
        <ChatAction text="清除聊天" icon={<ClearOutlined />} onClick={() => {
            chatStore.updateCurrentSession((session) => {
                if (session.clearContextIndex === session.messages.length) {
                    session.clearContextIndex = undefined;
                } else {
                    session.clearContextIndex = session.messages.length;
                }
            });
        }}/>
    </div>
}
