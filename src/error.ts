export class AppError extends Error {
  public status: number
  constructor(data: {
    message: string,
    status: number;
  }) {
    super(data.message);
    this.name = 'AppError';
    this.status = data.status
  }
}
