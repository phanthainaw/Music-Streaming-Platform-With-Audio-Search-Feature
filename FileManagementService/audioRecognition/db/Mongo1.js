const { MongoClient } = require('mongodb');
const { Couple } = require('../model/Couple');

class Mongo {
    constructor(uri, database) {
        this.uri = uri;
        this.databaseName = database;
        this.mongoClient = new MongoClient(this.uri);
        this.mongoDatabase = null;
        this.collectionName = 'AddressHashTable';
    }

    // Establish a connection to the MongoDB server
    async connect() {
        if (!this.mongoDatabase) {
            try {   
                await this.mongoClient.connect();
                this.mongoDatabase = this.mongoClient.db(this.databaseName);
                console.log('Connected to MongoDB');
            } catch (err) {
                console.error('Error connecting to MongoDB:', err);
                throw err;
            }
        }
    }

    async getAll(){
        const session = this.mongoClient.startSession();
        session.startTransaction();
        const collection =  await this.mongoDatabase.collection(this.collectionName);
        let result = await collection.find();
        console.log(result.toArray());
        session.endSession();
    }

    // Insert fingerprints into the MongoDB collection
    async insertFingerprints(fingerprints) {
        const collection = this.mongoDatabase.collection(this.collectionName);

        for (const [hash, couples] of Object.entries(fingerprints)) {
            try {
                if (!Array.isArray(couples)) {
                    console.error(`Invalid data format for hash ${hash}, expected array of couples`);
                    continue;
                }

                const result = await collection.updateOne(
                    { _id: parseInt(hash) },
                    { $addToSet: { Couples: { $each: couples } } },
                    { upsert: true }
                );

                if (result.upsertedId == null && result.matchedCount === 0) {
                    console.log(`Unable to update hash ${hash}`);
                }
            } catch (err) {
                console.error(`Error inserting fingerprints for hash ${hash}:`, err);
            }
        }
    }

    // Get matching couples for each hash
    async getMatchingCouples(hashes) {
        const collection = this.mongoDatabase.collection(this.collectionName);
        const matchedCouples = {};

        for (const hash of hashes) {
            try {
                const matchdoc = await collection.findOne({ _id: parseInt(hash) });

                if (matchdoc != null) {
                    const couplesForAHash = matchdoc.Couples.map(coupledoc => new Couple(coupledoc.Timestamp, coupledoc.SongId));
                    matchedCouples[hash] = couplesForAHash;
                } else {
                    console.log(`Can't find any matching couples for hash ${hash}`);
                }
            } catch (err) {
                console.error(`Error getting matched couples for hash ${hash}:`, err);
            }
        }

        return matchedCouples;
    }

    // Close the MongoDB client connection
    async close() {
        try {
            await this.mongoClient.close();
            console.log('MongoDB connection closed');
        } catch (err) {
            console.error('Error closing the MongoDB client:', err);
        }
    }
}

module.exports = {Mongo};
