import { IUserFeedback, NewUserFeedback } from './user-feedback.model';

export const sampleWithRequiredData: IUserFeedback = {
  id: 45268,
  rating: 5,
};

export const sampleWithPartialData: IUserFeedback = {
  id: 54800,
  rating: 6,
  suggestion: 'AGP lavender Games',
};

export const sampleWithFullData: IUserFeedback = {
  id: 65903,
  rating: 3,
  suggestion: 'online',
};

export const sampleWithNewData: NewUserFeedback = {
  rating: 7,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
