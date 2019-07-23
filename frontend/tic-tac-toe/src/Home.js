import React from 'react';
import './App.css';

import { useState } from 'react';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Button from '@material-ui/core/Button';

import Skeleton from './Skeleton.js';
import WinLoseSnackbar from './WinLoseSnackbar.js';

var ReactFitText = require('react-fittext');

const myStyles = (theme) => ({
  boardBox: {
    display: "inline-block",
    width: "85%",
    [theme.breakpoints.up('sm')]: {
      width: "60%",
    },
    maxWidth: "450px",
  },
});

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1),
  },
  toolbar: theme.mixins.toolbar,
  myContainer: {
    textAlign: "center",
    [theme.breakpoints.up('sm')]: {
      marginLeft: "120px",
    },
    [theme.breakpoints.up('md')]: {
      marginLeft: "70px",
    },
  },
}));

class Square extends React.Component {

  valueToChar = () => {
    if (this.props.value === 1) {
      return 'X';
    } else if (this.props.value === 2){
      return 'O';
    } else {
      return ' ';
    }
  }

  clickSquare = () => {
    // send move
    let json = { field: this.props.fieldNumber };
    fetch('/api/game/' + this.props.id + '/send-move/', {
      method: 'post',
      body: JSON.stringify(json)
    });
    this.props.callBack(this.props.fieldNumber);
  }

  render() {
    return (
      <div className = "square-box">
        <button
          className="square"
          onClick={this.clickSquare}
        >
          <ReactFitText compressor={0.25}>
          <h1>{this.valueToChar()}</h1>
          </ReactFitText>
        </button>
      </div>
    );
  }
}

class Board_ extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      board: [0, 0, 0, 0, 0, 0, 0, 0, 0],
      result: ' '
    };
    const { classes } = props;
    this.classes = classes;
  }

  componentDidMount = () => {
    this.timer = setInterval(this.updateBoard, 1000);
  }

  renderSquare = (i) => {
    return <Square fieldNumber={i} value={this.state.board[i]}
                   callBack={this.squareClicked} id={this.props.id}/>;
  }

  squareClicked = (i) => {
    let b = this.state.board;
    if (b[i] === 0) {
      b[i] = 1;
      this.setState({board: b});
    }
  }
  
  updateBoard = () => {
    // update board
    fetch('/api/game/' + this.props.id + '/get-move')
      .then((response) => response.json())
      .then(json => {
        this.setState({ board: json.state, result: json.result });
      }).catch(() => {
        console.log("Error update board");
      });
  }

  render() {
    return (
      <div className="board-outer-box">
        <div className={this.classes.boardBox}>
          <table className="board">
            <tbody>
              <tr className="board-row"><td className="board-row">
                {this.renderSquare(0)}
                {this.renderSquare(1)}
                {this.renderSquare(2)}
              </td></tr>
              <tr className="board-row"><td className="board-row">
                {this.renderSquare(3)}
                {this.renderSquare(4)}
                {this.renderSquare(5)}
              </td></tr>
              <tr className="board-row"><td className="board-row">
                {this.renderSquare(6)}
                {this.renderSquare(7)}
                {this.renderSquare(8)}
              </td></tr>
            </tbody>
          </table>
        </div>
        <WinLoseSnackbar result={this.state.result} />
      </div>
    );
  }
}

const Board = withStyles(myStyles)(Board_);

export default function Home() {

  const [state, setState] = useState({
    id: "",
  });

  function startGame() {
    fetch('/api/game/start-game')
      .then((response) => response.json())
      .then(json => {
        setState({ ...state, id: json.id });
      }).catch(() => {
        console.log("Error start game");
      });

  }   

  const classes = useStyles();

  return (
    <div>
      <Skeleton /> 
      <Container className={classes.myContainer}>
        <div className={classes.toolbar}/>
        <p> It's your turn </p>
        <Board id={state.id}/>
        <Button variant="contained" color="primary" className={classes.button} onClick={startGame}>
          Reset Game
        </Button>
        <Button variant="contained" color="secondary" className={classes.button} onClick={startGame}>
          Start Game
        </Button>
      </Container>
    </div>
  );
}


