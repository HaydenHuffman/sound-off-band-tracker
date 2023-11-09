import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
// import './App.css'
import ButtonAppBar from './components/ButtonAppBar'
import SignIn from './components/SignIn'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
        <ButtonAppBar />
        <SignIn />
    </>
  )
}

export default App
