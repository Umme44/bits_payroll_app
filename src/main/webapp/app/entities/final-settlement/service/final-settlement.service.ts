import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFinalSettlement, NewFinalSettlement } from '../final-settlement.model';
import { PfGfStatement } from '../../../shared/model/pf-gf/pf-gf-statement.model';
import { PfStatement } from '../../../shared/model/pf/pf-statement.model';

export type PartialUpdateFinalSettlement = Partial<IFinalSettlement> & Pick<IFinalSettlement, 'id'>;

type RestOf<T extends IFinalSettlement | NewFinalSettlement> = Omit<
  T,
  'dateOfResignation' | 'lastWorkingDay' | 'dateOfRelease' | 'createdAt' | 'updatedAt' | 'finalSettlementDate'
> & {
  dateOfResignation?: string | null;
  lastWorkingDay?: string | null;
  dateOfRelease?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  finalSettlementDate?: string | null;
};

export type RestFinalSettlement = RestOf<IFinalSettlement>;

export type NewRestFinalSettlement = RestOf<NewFinalSettlement>;

export type PartialUpdateRestFinalSettlement = RestOf<PartialUpdateFinalSettlement>;

export type EntityResponseType = HttpResponse<IFinalSettlement>;
export type EntityArrayResponseType = HttpResponse<IFinalSettlement[]>;

@Injectable({ providedIn: 'root' })
export class FinalSettlementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/final-settlements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(finalSettlement: NewFinalSettlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(finalSettlement);
    return this.http
      .post<RestFinalSettlement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(finalSettlement: IFinalSettlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(finalSettlement);
    return this.http
      .put<RestFinalSettlement>(`${this.resourceUrl}/${this.getFinalSettlementIdentifier(finalSettlement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(finalSettlement: PartialUpdateFinalSettlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(finalSettlement);
    return this.http
      .patch<RestFinalSettlement>(`${this.resourceUrl}/${this.getFinalSettlementIdentifier(finalSettlement)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFinalSettlement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFinalSettlement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPfGfStatement(employeeId: number): Observable<PfGfStatement> {
    return this.http.get<PfGfStatement>(`${SERVER_API_URL}api/payroll-mgt/pf-gf-statement/${employeeId}`);
  }

  getPfStatement(employeeId: number): Observable<PfStatement> {
    return this.http.get<PfStatement>(`${SERVER_API_URL}api/payroll-mgt/pf-statement/${employeeId}`);
  }

  generateAndSaveFinalSettlement(employeeId: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFinalSettlement>(`${SERVER_API_URL}api/payroll-mgt/generate-final-settlement/${employeeId}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  getFinalSettlementIdentifier(finalSettlement: Pick<IFinalSettlement, 'id'>): number {
    return finalSettlement.id;
  }

  compareFinalSettlement(o1: Pick<IFinalSettlement, 'id'> | null, o2: Pick<IFinalSettlement, 'id'> | null): boolean {
    return o1 && o2 ? this.getFinalSettlementIdentifier(o1) === this.getFinalSettlementIdentifier(o2) : o1 === o2;
  }

  addFinalSettlementToCollectionIfMissing<Type extends Pick<IFinalSettlement, 'id'>>(
    finalSettlementCollection: Type[],
    ...finalSettlementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const finalSettlements: Type[] = finalSettlementsToCheck.filter(isPresent);
    if (finalSettlements.length > 0) {
      const finalSettlementCollectionIdentifiers = finalSettlementCollection.map(
        finalSettlementItem => this.getFinalSettlementIdentifier(finalSettlementItem)!
      );
      const finalSettlementsToAdd = finalSettlements.filter(finalSettlementItem => {
        const finalSettlementIdentifier = this.getFinalSettlementIdentifier(finalSettlementItem);
        if (finalSettlementCollectionIdentifiers.includes(finalSettlementIdentifier)) {
          return false;
        }
        finalSettlementCollectionIdentifiers.push(finalSettlementIdentifier);
        return true;
      });
      return [...finalSettlementsToAdd, ...finalSettlementCollection];
    }
    return finalSettlementCollection;
  }

  protected convertDateFromClient<T extends IFinalSettlement | NewFinalSettlement | PartialUpdateFinalSettlement>(
    finalSettlement: T
  ): RestOf<T> {
    return {
      ...finalSettlement,
      dateOfResignation: finalSettlement.dateOfResignation?.format(DATE_FORMAT) ?? null,
      lastWorkingDay: finalSettlement.lastWorkingDay?.format(DATE_FORMAT) ?? null,
      dateOfRelease: finalSettlement.dateOfRelease?.format(DATE_FORMAT) ?? null,
      createdAt: finalSettlement.createdAt?.format(DATE_FORMAT) ?? null,
      updatedAt: finalSettlement.updatedAt?.format(DATE_FORMAT) ?? null,
      finalSettlementDate: finalSettlement.finalSettlementDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFinalSettlement: RestFinalSettlement): IFinalSettlement {
    return {
      ...restFinalSettlement,
      dateOfResignation: restFinalSettlement.dateOfResignation ? dayjs(restFinalSettlement.dateOfResignation) : undefined,
      lastWorkingDay: restFinalSettlement.lastWorkingDay ? dayjs(restFinalSettlement.lastWorkingDay) : undefined,
      dateOfRelease: restFinalSettlement.dateOfRelease ? dayjs(restFinalSettlement.dateOfRelease) : undefined,
      createdAt: restFinalSettlement.createdAt ? dayjs(restFinalSettlement.createdAt) : undefined,
      updatedAt: restFinalSettlement.updatedAt ? dayjs(restFinalSettlement.updatedAt) : undefined,
      finalSettlementDate: restFinalSettlement.finalSettlementDate ? dayjs(restFinalSettlement.finalSettlementDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFinalSettlement>): HttpResponse<IFinalSettlement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFinalSettlement[]>): HttpResponse<IFinalSettlement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
