import { IUser } from 'app/entities/user/user.model';

export interface IUserFeedback {
  id: number;
  rating?: number | null;
  suggestion?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewUserFeedback = Omit<IUserFeedback, 'id'> & { id: null };
