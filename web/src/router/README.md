# Router
> Proposal
Trying to reproduce exactly the same behaviour as Vue.js

## Setup
Prepare route list
```js
const routes = [
  {
    name: 'home',    // react-router-dom prop
    path: '/',       // react-router-dom prop
    exact: true,     // react-router-dom prop
    component: View, // React component to be rendered
    meta: {
      authRequired: true, // (optional) user must be logged
      role: 40,           // (optional) Minimun role required (gitlab roles)
      owneronly: true,    // (optional) only owner (override other rules)
      // other optional, e.g. newDataset: true
    },
  },
  {
    name: 'project',
    path: '/:namespace/:slug',
    component: ProjectView,
  }
]
```

Then in `index.jsx`
```js
  // ...
  <div className="main-container mb-5">
    <Router routes={routes} />
  </div>
</PersistGate>
```

## Features

### Named routes
Standard behavior allows link using the route's name. The primary advantage consists
in permit to change route path safety for routes that have been linked by names
consistently.

But it was necessary to use an extra

e.g.
```js
// index.js
import Router from 'router';
import routes from './routes';
```

```js
// ...
  <Router routes={routes} />
// ...
```


### Simplest Router implementation
This is compatible with *Link* component from *react-router-dom*.

Only [*RouterSimple*](/Router.jsx#9) component involved, thus maintenance is simpler.

e.g.
```js
// index.js
import { RouterSimple as Router } from 'router';
import routes from './routes';
```

```js
// ...
  <Router routes={routes} />
// ...
```



### Agnostic components
This means that *Router* and *Link* don't have contact with *application routes* until
they are passed by *props*.

This is useful for during unit testing to avoid import undesired components from *routes.js*


## Use cases

```js

// this route can be used or:
<Link to="/groupname/any-random-project/-/experiments/21" />

// can be replaced by this one:
<Link
  to={}
/>
```

### History push

```js
const history = useHistory()

// this push can be used or:
history.push('/groupname/any-random-project/-/experiments/21#all')

// can be replaced by this one:
history.push({ name: 'experiment', hash: 'all', params: {
    namespace: 'groupname',
    slug: 'any-random-projec',
    expId: 21,
  }
})
```

### MemoryRouter
designed for testing, similar to *react-router-dom's* MemoryRouter

```js
import React from 'react';
import { Provider } from 'react-redux';
import { MemoryRouter } from 'router';


<Provider store={store}>
  <MemoryRouter routes={routes} initialEntries={['route-to-test']} />
</Provider>,
```
