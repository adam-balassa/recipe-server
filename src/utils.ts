import { PutObjectCommand, S3Client } from '@aws-sdk/client-s3';
import { v4 as uuid } from 'uuid';
import axios from 'axios';

export function generateId() {
  return uuid();
}

const s3Client = new S3Client();

// export async function uploadImage(image) {
//
// }

export async function transferImage(imageUrl: string) {
  const response = await axios.get(imageUrl, { responseType: 'arraybuffer' });
  const id = generateId();
  const bucket = 'recipe-app-objects';
  await s3Client.send(new PutObjectCommand({
    Bucket: bucket,
    Key: `images/${id}`,
    Body: Buffer.from(response.data, 'binary'),
    ACL: 'public-read'
  }));
  return `https://${bucket}.s3.eu-central-1.amazonaws.com/images/${id}`;
}
