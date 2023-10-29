import { PutObjectCommand, S3Client } from '@aws-sdk/client-s3'
import { v4 as uuid } from 'uuid'

export function generateId() {
  return uuid()
}

const s3Client = new S3Client()

export async function uploadImage(imageUrl: string) {
  const imageBlob = await fetch(imageUrl).then(response => response.blob())
  const id = generateId()
  const bucket = 'recipe-app-objects'
  await s3Client.send(new PutObjectCommand({
    Bucket: bucket,
    Key: `images/${id}`,
    Body: Buffer.from(new Buffer(await imageBlob.arrayBuffer())),
    ACL: 'public-read'
  }))
  return `https://${bucket}.s3.eu-central-1.amazonaws.com/images/${id}`
}
