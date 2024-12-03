import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserFeedback, NewUserFeedback } from '../user-feedback.model';

export type PartialUpdateUserFeedback = Partial<IUserFeedback> & Pick<IUserFeedback, 'id'>;

export type EntityResponseType = HttpResponse<IUserFeedback>;
export type EntityArrayResponseType = HttpResponse<IUserFeedback[]>;

@Injectable({ providedIn: 'root' })
export class UserFeedbackService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-feedbacks');
  public resourceUrlCommon = this.applicationConfigService.getEndpointFor('api/common/user-feedbacks');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  isAllowed(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(this.resourceUrlCommon + '/is-allowed', { observe: 'response' });
  }

  create(userFeedback: NewUserFeedback): Observable<EntityResponseType> {
    return this.http.post<IUserFeedback>(this.resourceUrl, userFeedback, { observe: 'response' });
  }

  createCommon(userFeedback: IUserFeedback): Observable<EntityResponseType> {
    return this.http.post<IUserFeedback>(this.resourceUrlCommon, userFeedback, { observe: 'response' });
  }

  update(userFeedback: IUserFeedback): Observable<EntityResponseType> {
    return this.http.put<IUserFeedback>(`${this.resourceUrl}/${this.getUserFeedbackIdentifier(userFeedback)}`, userFeedback, {
      observe: 'response',
    });
  }

  partialUpdate(userFeedback: PartialUpdateUserFeedback): Observable<EntityResponseType> {
    return this.http.patch<IUserFeedback>(`${this.resourceUrl}/${this.getUserFeedbackIdentifier(userFeedback)}`, userFeedback, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserFeedback>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserFeedback[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserFeedbackIdentifier(userFeedback: Pick<IUserFeedback, 'id'>): number {
    return userFeedback.id;
  }

  compareUserFeedback(o1: Pick<IUserFeedback, 'id'> | null, o2: Pick<IUserFeedback, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserFeedbackIdentifier(o1) === this.getUserFeedbackIdentifier(o2) : o1 === o2;
  }

  addUserFeedbackToCollectionIfMissing<Type extends Pick<IUserFeedback, 'id'>>(
    userFeedbackCollection: Type[],
    ...userFeedbacksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userFeedbacks: Type[] = userFeedbacksToCheck.filter(isPresent);
    if (userFeedbacks.length > 0) {
      const userFeedbackCollectionIdentifiers = userFeedbackCollection.map(
        userFeedbackItem => this.getUserFeedbackIdentifier(userFeedbackItem)!
      );
      const userFeedbacksToAdd = userFeedbacks.filter(userFeedbackItem => {
        const userFeedbackIdentifier = this.getUserFeedbackIdentifier(userFeedbackItem);
        if (userFeedbackCollectionIdentifiers.includes(userFeedbackIdentifier)) {
          return false;
        }
        userFeedbackCollectionIdentifiers.push(userFeedbackIdentifier);
        return true;
      });
      return [...userFeedbacksToAdd, ...userFeedbackCollection];
    }
    return userFeedbackCollection;
  }
}
