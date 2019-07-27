import React from 'react';
import './App.css';

import { useState, useEffect } from 'react';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';

import Skeleton from './Skeleton.js';
import WinLoseSnackbar from './WinLoseSnackbar.js';

var ReactFitText = require('react-fittext');

const myStyles = (theme) => ({
  square: {
    background: "#fff",
    border: "1px solid #444",
    fontSize: "20vw",
    fontWeight: "bold",
    marginRight: "-1px",
    marginTop: "-1px",
    textAlign: "center",
    /* for 1x1 ratio */
    position: "absolute",
    left: "0",
    right: "0",
    top: "0",
    bottom: "0",
    width: "100%",
  },
  /* for 1x1 ratio of Square */
  squareBox: {
    position: "relative",
    width: "33.3%",
    display: "inline-block",
    "&:before": {
      content: '""',
      display: "inline-block",
      paddingTop: "100%",
    }
  },
  boardRow: {
    padding: "0",
    width: "100%",
  },
  board: {
    borderCollapse: "collapse",
    borderSpacing: "0",
    width: "100%",
  },
});

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1),
  },
  toolbar: theme.mixins.toolbar,
  myContainer: {
    textAlign: "center",
    paddingTop: theme.spacing(3),
    [theme.breakpoints.up('sm')]: {
      paddingLeft: (theme.spacing(1) + 240) + "px",
    },
  },
  boardBox: {
    display: "inline-block",
    width: "85%",
    [theme.breakpoints.up('md')]: {
      width: "60%",
    },
    maxWidth: "450px",
  },
  paper: {
    padding: theme.spacing(3),
  },
}));

class Square_ extends React.Component {

  constructor(props) {
    super(props);
    const { classes } = props;    
    this.classes = classes;
  }

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
    let json = { field: this.props.fieldNumber };
    fetch('/api/game/' + this.props.id + '/send-move/', {
      method: 'post',
      body: JSON.stringify(json)
    });
    this.props.callBack(this.props.fieldNumber);
  }

  render() {
    return (
      <div className={this.classes.squareBox}>
        <button
          className={this.classes.square}
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
      <div>
        <table className={this.classes.board}>
          <tbody>
            <tr className={this.classes.boardRow}><td className={this.classes.boardRow}>
              {this.renderSquare(0)}
              {this.renderSquare(1)}
              {this.renderSquare(2)}
            </td></tr>
            <tr className={this.classes.boardRow}><td className={this.classes.boardRow}>
              {this.renderSquare(3)}
              {this.renderSquare(4)}
              {this.renderSquare(5)}
            </td></tr>
            <tr className={this.boardRow}><td className={this.boardRow}>
              {this.renderSquare(6)}
              {this.renderSquare(7)}
              {this.renderSquare(8)}
            </td></tr>
          </tbody>
        </table>
        <WinLoseSnackbar result={this.state.result} />
      </div>
    );
  }
}

const Board = withStyles(myStyles)(Board_);
const Square = withStyles(myStyles)(Square_);

export default function Home() {

  const [state, setState] = useState({
    id: "",
  });

  useEffect(() => {
    if (state.id === "") {
      startGame();
    }
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
      <div className={classes.toolbar}/>
      <Container className={classes.myContainer}>
        <div className={classes.boardBox}>
          <Board id={state.id}/>
          <Button variant="contained" color="primary" className={classes.button} onClick={startGame}>
            Reset Game
          </Button>
          <Button variant="contained" color="secondary" className={classes.button} onClick={startGame}>
            Start Game
          </Button>
        </div>
      </Container>
    </div>
  );
}


