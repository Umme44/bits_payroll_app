export interface IUserFeedback {
  id?: number;
  rating?: number;
  suggestion?: string;
  userLogin?: string;
  userId?: number;
  userFirstName?: string;
  userLastName?: string;
}

export class UserFeedback implements IUserFeedback {
  constructor(public id?: number, public rating?: number, public suggestion?: string, public userLogin?: string, public userId?: number) {}
}
