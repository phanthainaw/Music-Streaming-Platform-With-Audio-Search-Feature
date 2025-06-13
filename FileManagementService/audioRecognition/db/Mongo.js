const mongoose = require("mongoose");
const { Schema, model } = mongoose;
const { Couple } = require("../model/Couple");

// Define the Couple schema
const coupleSchema = new Schema({
  SongId: Number,
  Timestamp: Number,
}, { _id: false });

// const Couple = model("Couple", coupleSchema);

const fingerprintSchema = new Schema({
  _id: Number,
  Couples: [coupleSchema]
}, { versionKey: false })

const Fingerprint = model("Fingerprint", fingerprintSchema);

// Define the main MongoDB class
class Mongo {
  constructor(uri) {
    this.uri = uri;
    this.collectionName = "Fingerprints";
    this.connection = null;
  }

  // Establish connection to MongoDB
  async connect() {
    try {
      this.connection = await mongoose.connect(this.uri);
      console.log("Connected to MongoDB");
    } catch (err) {
      console.error("Error connecting to MongoDB:", err);
      throw err;
    }
  }

  // Insert fingerprints into MongoDB
  async insertFingerprints(fingerprints) {
    for (const hash of Object.keys(fingerprints)) {
      const coupleDocs = fingerprints[hash].map(couple => ({
        SongId: couple.songId,
        Timestamp: couple.time
      }));
      const result = await Fingerprint.updateOne(
        { _id: parseInt(hash) },
        {
          $addToSet: { Couples: { $each: coupleDocs } },
        },
        { upsert: true }
      );
      if (result.upsertedCount === 0 && result.matchedCount === 0) {
        console.log(`Unable to update hash ${hash}`);
      }
    }
  }

  // Get matching couples from MongoDB
  async getMatchingCouples(hashes) {
    const matchedCouples = {};
    for (const hash of hashes) {
      const matchDoc = await Fingerprint.findOne({ _id: hash });
      if (matchDoc) {
        const couplesForAHash = matchDoc.Couples.map((coupleDoc) => {
          return new Couple(coupleDoc.Timestamp, coupleDoc.SongId);
        });
        matchedCouples[hash] = couplesForAHash;
      }
    }
    return matchedCouples;
  }

  async getAllDocuments(collectionName) {
    try {
      const collection = this.connection.connection.collection(collectionName);
      const documents = await collection.find().toArray();
      return documents;
    } catch (err) {
      console.error("Error fetching documents:", err);
      throw err;
    }
  }

  // Close the MongoDB connection
  async close() {
    try {
      await mongoose.connection.close();
      console.log("Disconnected from MongoDB");
    } catch (err) {
      console.error("Error disconnecting from MongoDB:", err);
    }
  }
}

module.exports = { Mongo };
