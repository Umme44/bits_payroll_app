import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrainingHistory, NewTrainingHistory } from '../training-history.model';

export type PartialUpdateTrainingHistory = Partial<ITrainingHistory> & Pick<ITrainingHistory, 'id'>;

type RestOf<T extends ITrainingHistory | NewTrainingHistory> = Omit<T, 'dateOfCompletion'> & {
  dateOfCompletion?: string | null;
};

export type RestTrainingHistory = RestOf<ITrainingHistory>;

export type NewRestTrainingHistory = RestOf<NewTrainingHistory>;

export type PartialUpdateRestTrainingHistory = RestOf<PartialUpdateTrainingHistory>;

export type EntityResponseType = HttpResponse<ITrainingHistory>;
export type EntityArrayResponseType = HttpResponse<ITrainingHistory[]>;

@Injectable({ providedIn: 'root' })
export class TrainingHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/training-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trainingHistory: ITrainingHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingHistory);
    return this.http
      .post<RestTrainingHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(trainingHistory: ITrainingHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingHistory);
    return this.http
      .put<RestTrainingHistory>(`${this.resourceUrl}/${this.getTrainingHistoryIdentifier(trainingHistory)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(trainingHistory: PartialUpdateTrainingHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trainingHistory);
    return this.http
      .patch<RestTrainingHistory>(this.resourceUrl, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTrainingHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrainingHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  queryByEmployeeId(employeeId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrainingHistory[]>(`${this.resourceUrl}/get-by-employee/${employeeId}`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrainingHistoryIdentifier(trainingHistory: Pick<ITrainingHistory, 'id'>): number {
    return trainingHistory.id;
  }

  compareTrainingHistory(o1: Pick<ITrainingHistory, 'id'> | null, o2: Pick<ITrainingHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrainingHistoryIdentifier(o1) === this.getTrainingHistoryIdentifier(o2) : o1 === o2;
  }

  addTrainingHistoryToCollectionIfMissing<Type extends Pick<ITrainingHistory, 'id'>>(
    trainingHistoryCollection: Type[],
    ...trainingHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trainingHistories: Type[] = trainingHistoriesToCheck.filter(isPresent);
    if (trainingHistories.length > 0) {
      const trainingHistoryCollectionIdentifiers = trainingHistoryCollection.map(
        trainingHistoryItem => this.getTrainingHistoryIdentifier(trainingHistoryItem)!
      );
      const trainingHistoriesToAdd = trainingHistories.filter(trainingHistoryItem => {
        const trainingHistoryIdentifier = this.getTrainingHistoryIdentifier(trainingHistoryItem);
        if (trainingHistoryCollectionIdentifiers.includes(trainingHistoryIdentifier)) {
          return false;
        }
        trainingHistoryCollectionIdentifiers.push(trainingHistoryIdentifier);
        return true;
      });
      return [...trainingHistoriesToAdd, ...trainingHistoryCollection];
    }
    return trainingHistoryCollection;
  }

  protected convertDateFromClient<T extends ITrainingHistory | NewTrainingHistory | PartialUpdateTrainingHistory>(
    trainingHistory: T
  ): RestOf<T> {
    return {
      ...trainingHistory,
      dateOfCompletion: trainingHistory.dateOfCompletion?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTrainingHistory: RestTrainingHistory): ITrainingHistory {
    return {
      ...restTrainingHistory,
      dateOfCompletion: restTrainingHistory.dateOfCompletion ? dayjs(restTrainingHistory.dateOfCompletion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrainingHistory>): HttpResponse<ITrainingHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrainingHistory[]>): HttpResponse<ITrainingHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
