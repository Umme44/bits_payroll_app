import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import dayjs from 'dayjs/esm';
import { map } from 'rxjs/operators';
import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFestivalBonusDetails, NewFestivalBonusDetails } from '../festival-bonus-details.model';
import { Filter } from '../../../common/employee-address-book/filter.model';
import { IFestival } from '../../festival/festival.model';
import { RestFestival } from '../../festival/service/festival.service';

export type PartialUpdateFestivalBonusDetails = Partial<IFestivalBonusDetails> & Pick<IFestivalBonusDetails, 'id'>;

export type EntityResponseType = HttpResponse<IFestivalBonusDetails>;
export type EntityArrayResponseType = HttpResponse<IFestivalBonusDetails[]>;

@Injectable({ providedIn: 'root' })
export class FestivalBonusDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payroll-mgt/festival-bonus-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(festivalBonusDetails: NewFestivalBonusDetails): Observable<EntityResponseType> {
    return this.http.post<IFestivalBonusDetails>(this.resourceUrl, festivalBonusDetails, { observe: 'response' });
  }

  update(festivalBonusDetails: IFestivalBonusDetails): Observable<EntityResponseType> {
    return this.http.put<IFestivalBonusDetails>(
      `${this.resourceUrl}/${this.getFestivalBonusDetailsIdentifier(festivalBonusDetails)}`,
      festivalBonusDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(festivalBonusDetails: PartialUpdateFestivalBonusDetails): Observable<EntityResponseType> {
    return this.http.patch<IFestivalBonusDetails>(
      `${this.resourceUrl}/${this.getFestivalBonusDetailsIdentifier(festivalBonusDetails)}`,
      festivalBonusDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFestivalBonusDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFestivalBonusDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFestivalBonusDetailsIdentifier(festivalBonusDetails: Pick<IFestivalBonusDetails, 'id'>): number {
    return festivalBonusDetails.id;
  }

  compareFestivalBonusDetails(o1: Pick<IFestivalBonusDetails, 'id'> | null, o2: Pick<IFestivalBonusDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getFestivalBonusDetailsIdentifier(o1) === this.getFestivalBonusDetailsIdentifier(o2) : o1 === o2;
  }

  addFestivalBonusDetailsToCollectionIfMissing<Type extends Pick<IFestivalBonusDetails, 'id'>>(
    festivalBonusDetailsCollection: Type[],
    ...festivalBonusDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const festivalBonusDetails: Type[] = festivalBonusDetailsToCheck.filter(isPresent);
    if (festivalBonusDetails.length > 0) {
      const festivalBonusDetailsCollectionIdentifiers = festivalBonusDetailsCollection.map(
        festivalBonusDetailsItem => this.getFestivalBonusDetailsIdentifier(festivalBonusDetailsItem)!
      );
      const festivalBonusDetailsToAdd = festivalBonusDetails.filter(festivalBonusDetailsItem => {
        const festivalBonusDetailsIdentifier = this.getFestivalBonusDetailsIdentifier(festivalBonusDetailsItem);
        if (festivalBonusDetailsCollectionIdentifiers.includes(festivalBonusDetailsIdentifier)) {
          return false;
        }
        festivalBonusDetailsCollectionIdentifiers.push(festivalBonusDetailsIdentifier);
        return true;
      });
      return [...festivalBonusDetailsToAdd, ...festivalBonusDetailsCollection];
    }
    return festivalBonusDetailsCollection;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((festivalBonusDetails: IFestivalBonusDetails) => {
        festivalBonusDetails.festivalDate = festivalBonusDetails.festivalDate ? dayjs(festivalBonusDetails.festivalDate) : undefined;
        festivalBonusDetails.bonusDisbursementDate = festivalBonusDetails.bonusDisbursementDate
          ? dayjs(festivalBonusDetails.bonusDisbursementDate)
          : undefined;
        festivalBonusDetails.doj = festivalBonusDetails.doj ? dayjs(festivalBonusDetails.doj) : undefined;
        festivalBonusDetails.doc = festivalBonusDetails.doc ? dayjs(festivalBonusDetails.doc) : undefined;
        festivalBonusDetails.contractPeriodEndDate = festivalBonusDetails.contractPeriodEndDate
          ? dayjs(festivalBonusDetails.contractPeriodEndDate)
          : undefined;
        festivalBonusDetails.contractPeriodExtendedTo = festivalBonusDetails.contractPeriodExtendedTo
          ? dayjs(festivalBonusDetails.contractPeriodExtendedTo)
          : undefined;
      });
    }
    return res;
  }

  findByFestivalId(festivalId: number, filterDto: Filter): Observable<EntityArrayResponseType> {
    return this.http
      .post<IFestivalBonusDetails[]>(this.resourceUrl + '/get-by-festival/' + festivalId, filterDto, { observe: 'response' })
      .pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  holdFbDetails(fbDetailsId: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrl}/hold/${fbDetailsId}`, { observe: 'response' });
  }

  unHoldFbDetails(fbDetailsId: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrl}/unHold/${fbDetailsId}`, { observe: 'response' });
  }

  festivalBonusHoldList(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFestivalBonusDetails[]>(`${this.resourceUrl}/festival-bonus-hold`, { params: options, observe: 'response' });
  }
}
