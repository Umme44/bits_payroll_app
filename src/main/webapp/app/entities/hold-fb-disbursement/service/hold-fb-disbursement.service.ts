import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHoldFbDisbursement, NewHoldFbDisbursement } from '../hold-fb-disbursement.model';
import { IHoldFbDisbursementApproval } from '../hold-fb-disbursement-approval.model';

export type PartialUpdateHoldFbDisbursement = Partial<IHoldFbDisbursement> & Pick<IHoldFbDisbursement, 'id'>;

type RestOf<T extends IHoldFbDisbursement | NewHoldFbDisbursement> = Omit<T, 'disbursedAt'> & {
  disbursedAt?: string | null;
};

export type RestHoldFbDisbursement = RestOf<IHoldFbDisbursement>;

export type NewRestHoldFbDisbursement = RestOf<NewHoldFbDisbursement>;

export type PartialUpdateRestHoldFbDisbursement = RestOf<PartialUpdateHoldFbDisbursement>;

export type EntityResponseType = HttpResponse<IHoldFbDisbursement>;
export type EntityArrayResponseType = HttpResponse<IHoldFbDisbursement[]>;

type EntityResponseTypeV2 = HttpResponse<IHoldFbDisbursementApproval>;

@Injectable({ providedIn: 'root' })
export class HoldFbDisbursementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/hold-fb-disbursements');

  public resourceUrlV2 = SERVER_API_URL + 'api/payroll-mgt/hold-festivalBonus-disbursements';

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(holdFbDisbursement: NewHoldFbDisbursement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holdFbDisbursement);
    return this.http
      .post<RestHoldFbDisbursement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(holdFbDisbursement: IHoldFbDisbursement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holdFbDisbursement);
    return this.http
      .put<RestHoldFbDisbursement>(`${this.resourceUrl}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(holdFbDisbursement: PartialUpdateHoldFbDisbursement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(holdFbDisbursement);
    return this.http
      .patch<RestHoldFbDisbursement>(`${this.resourceUrl}/${this.getHoldFbDisbursementIdentifier(holdFbDisbursement)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHoldFbDisbursement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHoldFbDisbursement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHoldFbDisbursementIdentifier(holdFbDisbursement: Pick<IHoldFbDisbursement, 'id'>): number {
    return holdFbDisbursement.id;
  }

  compareHoldFbDisbursement(o1: Pick<IHoldFbDisbursement, 'id'> | null, o2: Pick<IHoldFbDisbursement, 'id'> | null): boolean {
    return o1 && o2 ? this.getHoldFbDisbursementIdentifier(o1) === this.getHoldFbDisbursementIdentifier(o2) : o1 === o2;
  }

  addHoldFbDisbursementToCollectionIfMissing<Type extends Pick<IHoldFbDisbursement, 'id'>>(
    holdFbDisbursementCollection: Type[],
    ...holdFbDisbursementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const holdFbDisbursements: Type[] = holdFbDisbursementsToCheck.filter(isPresent);
    if (holdFbDisbursements.length > 0) {
      const holdFbDisbursementCollectionIdentifiers = holdFbDisbursementCollection.map(
        holdFbDisbursementItem => this.getHoldFbDisbursementIdentifier(holdFbDisbursementItem)!
      );
      const holdFbDisbursementsToAdd = holdFbDisbursements.filter(holdFbDisbursementItem => {
        const holdFbDisbursementIdentifier = this.getHoldFbDisbursementIdentifier(holdFbDisbursementItem);
        if (holdFbDisbursementCollectionIdentifiers.includes(holdFbDisbursementIdentifier)) {
          return false;
        }
        holdFbDisbursementCollectionIdentifiers.push(holdFbDisbursementIdentifier);
        return true;
      });
      return [...holdFbDisbursementsToAdd, ...holdFbDisbursementCollection];
    }
    return holdFbDisbursementCollection;
  }

  protected convertDateFromClient<T extends IHoldFbDisbursement | NewHoldFbDisbursement | PartialUpdateHoldFbDisbursement>(
    holdFbDisbursement: T
  ): RestOf<T> {
    return {
      ...holdFbDisbursement,
      disbursedAt: holdFbDisbursement.disbursedAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restHoldFbDisbursement: RestHoldFbDisbursement): IHoldFbDisbursement {
    return {
      ...restHoldFbDisbursement,
      disbursedAt: restHoldFbDisbursement.disbursedAt ? dayjs(restHoldFbDisbursement.disbursedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHoldFbDisbursement>): HttpResponse<IHoldFbDisbursement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHoldFbDisbursement[]>): HttpResponse<IHoldFbDisbursement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  protected convertDateFromClientForApproval(holdFbDisbursementApproval: IHoldFbDisbursementApproval): IHoldFbDisbursementApproval {
    const copy: IHoldFbDisbursementApproval = Object.assign({}, holdFbDisbursementApproval, {
      disbursedAt:
        holdFbDisbursementApproval.disbursedAt && holdFbDisbursementApproval.disbursedAt.isValid()
          ? holdFbDisbursementApproval.disbursedAt.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  createOld(holdFbDisbursementApproval: IHoldFbDisbursementApproval): Observable<EntityResponseType> {
    const copy = this.convertDateFromClientForApproval(holdFbDisbursementApproval);
    return this.http
      .post<IHoldFbDisbursementApproval>(this.resourceUrlV2, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServerV2(res)));
  }

  protected convertDateFromServerV2(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.disbursedAt = res.body.disbursedAt ? dayjs(res.body.disbursedAt) : undefined;
    }
    return res;
  }
}
