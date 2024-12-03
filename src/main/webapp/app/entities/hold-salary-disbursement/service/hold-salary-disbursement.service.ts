import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHoldSalaryDisbursement, NewHoldSalaryDisbursement } from '../hold-salary-disbursement.model';

export type PartialUpdateHoldSalaryDisbursement = Partial<IHoldSalaryDisbursement> & Pick<IHoldSalaryDisbursement, 'id'>;

type RestOf<T extends IHoldSalaryDisbursement | NewHoldSalaryDisbursement> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestHoldSalaryDisbursement = RestOf<IHoldSalaryDisbursement>;

export type NewRestHoldSalaryDisbursement = RestOf<NewHoldSalaryDisbursement>;

export type PartialUpdateRestHoldSalaryDisbursement = RestOf<PartialUpdateHoldSalaryDisbursement>;

export type EntityResponseType = HttpResponse<IHoldSalaryDisbursement>;
export type EntityArrayResponseType = HttpResponse<IHoldSalaryDisbursement[]>;

@Injectable({ providedIn: 'root' })
export class HoldSalaryDisbursementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/hold-salary-disbursements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(holdSalaryDisbursement: NewHoldSalaryDisbursement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holdSalaryDisbursement);
    return this.http
      .post<RestHoldSalaryDisbursement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(holdSalaryDisbursement: IHoldSalaryDisbursement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holdSalaryDisbursement);
    return this.http
      .put<RestHoldSalaryDisbursement>(`${this.resourceUrl}/${this.getHoldSalaryDisbursementIdentifier(holdSalaryDisbursement)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(holdSalaryDisbursement: PartialUpdateHoldSalaryDisbursement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holdSalaryDisbursement);
    return this.http
      .patch<RestHoldSalaryDisbursement>(`${this.resourceUrl}/${this.getHoldSalaryDisbursementIdentifier(holdSalaryDisbursement)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHoldSalaryDisbursement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHoldSalaryDisbursement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHoldSalaryDisbursementIdentifier(holdSalaryDisbursement: Pick<IHoldSalaryDisbursement, 'id'>): number {
    return holdSalaryDisbursement.id;
  }

  compareHoldSalaryDisbursement(o1: Pick<IHoldSalaryDisbursement, 'id'> | null, o2: Pick<IHoldSalaryDisbursement, 'id'> | null): boolean {
    return o1 && o2 ? this.getHoldSalaryDisbursementIdentifier(o1) === this.getHoldSalaryDisbursementIdentifier(o2) : o1 === o2;
  }

  addHoldSalaryDisbursementToCollectionIfMissing<Type extends Pick<IHoldSalaryDisbursement, 'id'>>(
    holdSalaryDisbursementCollection: Type[],
    ...holdSalaryDisbursementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const holdSalaryDisbursements: Type[] = holdSalaryDisbursementsToCheck.filter(isPresent);
    if (holdSalaryDisbursements.length > 0) {
      const holdSalaryDisbursementCollectionIdentifiers = holdSalaryDisbursementCollection.map(
        holdSalaryDisbursementItem => this.getHoldSalaryDisbursementIdentifier(holdSalaryDisbursementItem)!
      );
      const holdSalaryDisbursementsToAdd = holdSalaryDisbursements.filter(holdSalaryDisbursementItem => {
        const holdSalaryDisbursementIdentifier = this.getHoldSalaryDisbursementIdentifier(holdSalaryDisbursementItem);
        if (holdSalaryDisbursementCollectionIdentifiers.includes(holdSalaryDisbursementIdentifier)) {
          return false;
        }
        holdSalaryDisbursementCollectionIdentifiers.push(holdSalaryDisbursementIdentifier);
        return true;
      });
      return [...holdSalaryDisbursementsToAdd, ...holdSalaryDisbursementCollection];
    }
    return holdSalaryDisbursementCollection;
  }

  protected convertDateFromClient<T extends IHoldSalaryDisbursement | NewHoldSalaryDisbursement | PartialUpdateHoldSalaryDisbursement>(
    holdSalaryDisbursement: T
  ): RestOf<T> {
    return {
      ...holdSalaryDisbursement,
      date: holdSalaryDisbursement.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restHoldSalaryDisbursement: RestHoldSalaryDisbursement): IHoldSalaryDisbursement {
    return {
      ...restHoldSalaryDisbursement,
      date: restHoldSalaryDisbursement.date ? dayjs(restHoldSalaryDisbursement.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHoldSalaryDisbursement>): HttpResponse<IHoldSalaryDisbursement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHoldSalaryDisbursement[]>): HttpResponse<IHoldSalaryDisbursement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
