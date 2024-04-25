import React, { useState } from "react";
import Attach from "../img/attach.png";
import Img from "../img/img.png";
import "./ChatPage.css";

const Input = () => {
  const [text, setText] = useState("");

  const handleSendBtn = (e) => {
    const trimmedText = e.target.value.trim();
    setText(e.target.value);
    const send = document.querySelector(".sendbtn");
    if (trimmedText !== "" && trimmedText !== null) {
      send.style.backgroundColor = "blue";
    } else {
      send.style.backgroundColor = "#8da4f1";
    }
  };

  return (
    <div className="input">
      <input
        onChange={handleSendBtn}
        type="text"
        placeholder="Type something"
        title="Type a message"
        value={text}
      />
      <div className="send">
        <img src={Img} alt="" />
        <input type="file" name="" id="file" style={{ display: "none" }} />
        <label htmlFor="file">
          <img src={Attach} alt="" />
        </label>
        <button className="sendbtn">Send</button>
      </div>
    </div>
  );
};

export default Input;
