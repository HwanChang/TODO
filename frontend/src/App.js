import React, { Component } from 'react';
import { HashRouter, Route, Switch, BrowserRouter } from 'react-router-dom';
// import { renderRoutes } from 'react-router-config';
import './App.scss';

const loading = () => <div className="animated fadeIn pt-3 text-center">Loading...</div>;

// Containers
const DefaultLayout = React.lazy(() => import('./containers/DefaultLayout'));

// Pages
const Login = React.lazy(() => import('./views/Pages/Login'));
const Register = React.lazy(() => import('./views/Pages/Register'));
const Page404 = React.lazy(() => import('./views/Pages/Page404'));
const Page500 = React.lazy(() => import('./views/Pages/Page500'));

class App extends Component {

    render() {
        return (
            <BrowserRouter>
                <React.Suspense fallback={loading()}>
                    <Switch>
                        <Route exact path="/signin" name="Login Page" render={props => <Login {...props}/>} />
                        <Route exact path="/signup" name="Register Page" render={props => <Register {...props}/>} />
                        <Route exact path="/404" name="Page 404" render={props => <Page404 {...props}/>} />
                        <Route exact path="/500" name="Page 500" render={props => <Page500 {...props}/>} />
                        {/*<Route path="/" name="Home" render={props => <DefaultLayout {...props}/>} />*/}
                    </Switch>
                </React.Suspense>
            </BrowserRouter>
        );
    }
}

export default App;



// import React from 'react';
// import 'bootstrap/dist/css/bootstrap.min.css'
// import logo from './logo.svg';
// import './App.css';

// import TopMenuComponent from "./component/TopMenuComponent";

// function App() {
//     return (
//         <div className="App">
//             <div>
//                 <TopMenuComponent>
//                 </TopMenuComponent>
//             </div>
//         </div>
//     );
// }
//
// export default App;


// function App() {
//   return (
//     <div className="App">
//       <header className="App-header">
//         <img src={logo} className="App-logo" alt="logo" />
//         <p>
//           Edit <code>src/App.js</code> and save to reload.
//         </p>
//         <a
//           className="App-link"
//           href="https://reactjs.org"
//           target="_blank"
//           rel="noopener noreferrer"
//         >
//           Learn React
//         </a>
//       </header>
//     </div>
//   );
// }
//
// export default App;
