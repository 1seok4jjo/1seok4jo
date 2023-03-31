import {useEffect, useState} from 'react'
import axios from 'axios'

function App() {
  const [hello, setHello] = useState('')

  useEffect(() => {
    axios.get('http://localhost:8080/hello').then(response => setHello(response.data))
  }, [])

  return (
    <div>
      백엔드에서 가져온 데이터입니다!! : {hello}
      <br />
      완벽이해
    </div>
  )
}

export default App
