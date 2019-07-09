import React from 'react';
import './App.css';

import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';

import ButtonAppBar from './ButtonAppBar.js';

const useStyles = makeStyles(theme => ({
  button: {
    margin: theme.spacing(1),
  },
}));

class Square extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: null,
    };
  }

  render() {
    return (
      <div className = "square-box">
        <button
          className="square"
          onClick={() => this.setState({value: 'X'})}
        >
          {this.state.value}
        </button>
      </div>
    );
  }
}

class Board extends React.Component {
  renderSquare(i) {
    return <Square />;
  }

  render() {
    const status = 'Next player: X';

    return (
      <div className="board-box">
        <table className="board">
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
        </table>
      </div>
    );
  }
}

export default function Home() {
  const classes = useStyles();

  return (
    <div>
      <ButtonAppBar/>
      <Container style={{textAlign: "center"}}>
        <p> Status </p>
        <Board/>
        <Button variant="contained" color="primary" className={classes.button}>
          Reset Game
        </Button>
        <Button variant="contained" color="secondary" className={classes.button}>
          Start Game
        </Button>
      </Container>
    </div>
  );
}


