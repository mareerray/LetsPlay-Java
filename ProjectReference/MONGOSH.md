Here are useful mongosh commands to help you find data easily in your MongoDB database:

1. List All Users
   ````
   db.users.find({})
   Shows every user (document) in the users collection.

2. List All Products
   ````
   db.products.find({})
   Lists all products in the products collection.

3. Find All Users Who Have Logged In
   Assuming you store last login or a login flag, e.g. { loggedIn: true }:

    ````
    db.users.find({ loggedIn: true })
   
    Or, if you track with a lastLogin field:
    db.users.find({ lastLogin: { $exists: true } })

4. Find Individual User
   By Role:

    ````
    db.users.find({ role: "admin" })
   
    By Email:
    db.users.find({ email: "admin@example.com" })
   
    By ID:
    Replace <id> with the user’s ObjectId:
    db.users.find({ _id: ObjectId("68db922215c0ae1d33a5b3d2") })
   
5. Find Individual Product
   By Name:
    ````
    db.products.find({ name: "Product Name" })
   
    By Price:
    Find products at a specific price:
    

    db.products.find({ price: 19.99 })
    Find products with price greater than X:
    
    
    db.products.find({ price: { $gt: 100 } })
    Useful Tips
    Pretty Print Results:
    
    
    db.users.find({}).pretty()
    Limit results:
    
    
    db.users.find({}).limit(5)