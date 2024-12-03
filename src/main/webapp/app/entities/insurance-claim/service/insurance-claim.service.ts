import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInsuranceClaim, NewInsuranceClaim } from '../insurance-claim.model';

export type PartialUpdateInsuranceClaim = Partial<IInsuranceClaim> & Pick<IInsuranceClaim, 'id'>;

type RestOf<T extends IInsuranceClaim | NewInsuranceClaim> = Omit<
  T,
  'settlementDate' | 'paymentDate' | 'regretDate' | 'createdAt' | 'updatedAt'
> & {
  settlementDate?: string | null;
  paymentDate?: string | null;
  regretDate?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

export type RestInsuranceClaim = RestOf<IInsuranceClaim>;

export type NewRestInsuranceClaim = RestOf<NewInsuranceClaim>;

export type PartialUpdateRestInsuranceClaim = RestOf<PartialUpdateInsuranceClaim>;

export type EntityResponseType = HttpResponse<IInsuranceClaim>;
export type EntityArrayResponseType = HttpResponse<IInsuranceClaim[]>;

@Injectable({ providedIn: 'root' })
export class InsuranceClaimService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employee-mgt/insurance-claims');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(insuranceClaim: NewInsuranceClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(insuranceClaim);
    return this.http
      .post<RestInsuranceClaim>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(insuranceClaim: IInsuranceClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(insuranceClaim);
    return this.http
      .put<RestInsuranceClaim>(`${this.resourceUrl}/${this.getInsuranceClaimIdentifier(insuranceClaim)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(insuranceClaim: PartialUpdateInsuranceClaim): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(insuranceClaim);
    return this.http
      .patch<RestInsuranceClaim>(`${this.resourceUrl}/${this.getInsuranceClaimIdentifier(insuranceClaim)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInsuranceClaim>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInsuranceClaim[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInsuranceClaimIdentifier(insuranceClaim: Pick<IInsuranceClaim, 'id'>): number {
    return insuranceClaim.id;
  }

  compareInsuranceClaim(o1: Pick<IInsuranceClaim, 'id'> | null, o2: Pick<IInsuranceClaim, 'id'> | null): boolean {
    return o1 && o2 ? this.getInsuranceClaimIdentifier(o1) === this.getInsuranceClaimIdentifier(o2) : o1 === o2;
  }

  addInsuranceClaimToCollectionIfMissing<Type extends Pick<IInsuranceClaim, 'id'>>(
    insuranceClaimCollection: Type[],
    ...insuranceClaimsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const insuranceClaims: Type[] = insuranceClaimsToCheck.filter(isPresent);
    if (insuranceClaims.length > 0) {
      const insuranceClaimCollectionIdentifiers = insuranceClaimCollection.map(
        insuranceClaimItem => this.getInsuranceClaimIdentifier(insuranceClaimItem)!
      );
      const insuranceClaimsToAdd = insuranceClaims.filter(insuranceClaimItem => {
        const insuranceClaimIdentifier = this.getInsuranceClaimIdentifier(insuranceClaimItem);
        if (insuranceClaimCollectionIdentifiers.includes(insuranceClaimIdentifier)) {
          return false;
        }
        insuranceClaimCollectionIdentifiers.push(insuranceClaimIdentifier);
        return true;
      });
      return [...insuranceClaimsToAdd, ...insuranceClaimCollection];
    }
    return insuranceClaimCollection;
  }

  protected convertDateFromClient<T extends IInsuranceClaim | NewInsuranceClaim | PartialUpdateInsuranceClaim>(
    insuranceClaim: T
  ): RestOf<T> {
    return {
      ...insuranceClaim,
      settlementDate: insuranceClaim.settlementDate?.format(DATE_FORMAT) ?? null,
      paymentDate: insuranceClaim.paymentDate?.format(DATE_FORMAT) ?? null,
      regretDate: insuranceClaim.regretDate?.format(DATE_FORMAT) ?? null,
      createdAt: insuranceClaim.createdAt?.toJSON() ?? null,
      updatedAt: insuranceClaim.updatedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restInsuranceClaim: RestInsuranceClaim): IInsuranceClaim {
    return {
      ...restInsuranceClaim,
      settlementDate: restInsuranceClaim.settlementDate ? dayjs(restInsuranceClaim.settlementDate) : undefined,
      paymentDate: restInsuranceClaim.paymentDate ? dayjs(restInsuranceClaim.paymentDate) : undefined,
      regretDate: restInsuranceClaim.regretDate ? dayjs(restInsuranceClaim.regretDate) : undefined,
      createdAt: restInsuranceClaim.createdAt ? dayjs(restInsuranceClaim.createdAt) : undefined,
      updatedAt: restInsuranceClaim.updatedAt ? dayjs(restInsuranceClaim.updatedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInsuranceClaim>): HttpResponse<IInsuranceClaim> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInsuranceClaim[]>): HttpResponse<IInsuranceClaim[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
