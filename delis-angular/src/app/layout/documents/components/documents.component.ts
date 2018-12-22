import { Component, OnInit } from '@angular/core';
import { TranslateService } from "@ngx-translate/core";

import { routerTransition } from '../../../router.animations';
import { DocumentsService } from '../services/documents.service';
import { DocumentsModel, FilterProcessResult } from '../models/documents.model';
import { DateRangeModel } from '../../../models/date.range.model';
import { LocaleService } from "../../../service/locale.service";
import { PageContainerModel } from "../../../models/page.container.model";
import { environment } from "../../../../environments/environment";
import { documentTypes } from '../models/documents.view.model';
import { ingoingFormats } from '../models/documents.view.model';
import { lastErrors } from '../models/documents.view.model';
import { statuses } from '../models/documents.view.model';
import { TableHeaderSortModel } from "../../bs-component/components/table-header-sort/table.header.sort.model";

@Component({
    selector: 'app-documents',
    templateUrl: './documents.component.html',
    styleUrls: ['./documents.component.scss'],
    animations: [routerTransition()]
})
export class DocumentsComponent implements OnInit {

    env = environment;

    selectedStatus: any;
    selectedLastError: any;
    selectedDocumentType: any;
    selectedIngoingFormat: any;
    selectedPageSize: any;
    textOrganisation: string;
    textReceiver: string;
    textSenderName: string;
    textReceiverName: string;

    documents: DocumentsModel[];
    tableHeaderSortModels: TableHeaderSortModel[] = [];
    statuses = statuses;
    documentTypes = documentTypes;
    ingoingFormats = ingoingFormats;
    lastErrors = lastErrors;
    filter: FilterProcessResult;
    container: any;

    pageSizes = [
        {pageSize: 5},
        {pageSize: 10},
        {pageSize: 20},
        {pageSize: 50},
        {pageSize: 100}
    ];

    constructor(private translate: TranslateService, private documentsService: DocumentsService, private locale: LocaleService) {
        this.translate.use(locale.getlocale().match(/en|da/) ? locale.getlocale() : 'en');
    }

    ngOnInit() {
        this.container = new PageContainerModel<DocumentsModel>();
        this.initDefaultValues();
        if (this.env.production) {
            this.currentProdDocuments(1, 10);
        } else {
            this.currentDevDocuments(1, 10);
        }
    }

    initDefaultValues() {

        this.selectedStatus = {status: 'ALL', selected: true};
        this.selectedLastError = {lastError: 'ALL', selected: true};
        this.selectedIngoingFormat = {ingoingFormat: 'ALL', selected: true};
        this.selectedDocumentType = {documentType: 'ALL', selected: true};
        this.selectedPageSize = {pageSize: 10, selected: true};
        this.filter = new FilterProcessResult();
        this.tableHeaderSortModels.push(
            {
                columnName: 'Organisation', columnClick: 0
            },
            {
                columnName: 'Receiver', columnClick: 0
            },
            {
                columnName: 'Status', columnClick: 0
            },
            {
                columnName: 'Last Error', columnClick: 0
            },
            {
                columnName: 'Document Type', columnClick: 0
            },
            {
                columnName: 'Ingoing Format', columnClick: 0
            },
            {
                columnName: 'Received', columnClick: 0
            },
            {
                columnName: 'Issued', columnClick: 0
            },
            {
                columnName: 'Sender Name', columnClick: 0
            },
            {
                columnName: 'Receiver Name', columnClick: 0
            }
        );
    }

    currentProdDocuments(currentPage: number, sizeElement: number) {
        this.documentsService.getAnyDocuments(currentPage, sizeElement, this.filter).subscribe(
            (data: {}) => {
                this.container.collectionSize = data["collectionSize"];
                this.container.currentPage = data["currentPage"];
                this.container.pageSize = data["pageSize"];
                this.documents = data["items"];
            }
        );
    }

    currentDevDocuments(currentPage: number, sizeElement: number) {
        this.documents = this.documentsService.loadDocumentsJSON();

        if (this.filter.status !== 'ALL') {
            this.documents = this.documents.filter(el => el.status === this.filter.status);
        }

        if (this.filter.lastError !== 'ALL') {
            this.documents = this.documents.filter(el => el.lastError === this.filter.lastError);
        }

        if (this.filter.documentType !== 'ALL') {
            this.documents = this.documents.filter(el => el.documentType === this.filter.documentType);
        }

        if (this.filter.ingoingFormat !== 'ALL') {
            this.documents = this.documents.filter(el => el.ingoingFormat === this.filter.ingoingFormat);
        }

        if (this.filter.countClickOrganisation === 1) {
            this.documents.sort((one, two) => (one.organisation < two.organisation ? -1 : 1));
        } else if (this.filter.countClickOrganisation === 2) {
            this.documents.sort((one, two) => (one.organisation > two.organisation ? -1 : 1));
        }

        if (this.filter.countClickReceiver === 1) {
            this.documents.sort((one, two) => (one.receiver < two.receiver ? -1 : 1));
        } else if (this.filter.countClickReceiver === 2) {
            this.documents.sort((one, two) => (one.receiver > two.receiver ? -1 : 1));
        }

        if (this.filter.countClickStatus === 1) {
            this.documents.sort((one, two) => (one.status < two.status ? -1 : 1));
        } else if (this.filter.countClickStatus === 2) {
            this.documents.sort((one, two) => (one.status > two.status ? -1 : 1));
        }

        if (this.filter.countClickLastError === 1) {
            this.documents.sort((one, two) => (one.lastError < two.lastError ? -1 : 1));
        } else if (this.filter.countClickLastError === 2) {
            this.documents.sort((one, two) => (one.lastError > two.lastError ? -1 : 1));
        }

        if (this.filter.countClickDocumentType === 1) {
            this.documents.sort((one, two) => (one.documentType < two.documentType ? -1 : 1));
        } else if (this.filter.countClickDocumentType === 2) {
            this.documents.sort((one, two) => (one.documentType > two.documentType ? -1 : 1));
        }

        if (this.filter.countClickIngoingFormat === 1) {
            this.documents.sort((one, two) => (one.ingoingFormat < two.ingoingFormat ? -1 : 1));
        } else if (this.filter.countClickIngoingFormat === 2) {
            this.documents.sort((one, two) => (one.ingoingFormat > two.ingoingFormat ? -1 : 1));
        }

        if (this.filter.countClickReceived === 1) {
            this.documents.sort((one, two) => (one.received < two.received ? -1 : 1));
        } else if (this.filter.countClickReceived === 2) {
            this.documents.sort((one, two) => (one.received > two.received ? -1 : 1));
        }

        if (this.filter.countClickIssued === 1) {
            this.documents.sort((one, two) => (one.issued < two.issued ? -1 : 1));
        } else if (this.filter.countClickIssued === 2) {
            this.documents.sort((one, two) => (one.issued > two.issued ? -1 : 1));
        }

        if (this.filter.countClickSenderName === 1) {
            this.documents.sort((one, two) => (one.senderName < two.senderName ? -1 : 1));
        } else if (this.filter.countClickSenderName === 2) {
            this.documents.sort((one, two) => (one.senderName > two.senderName ? -1 : 1));
        }

        if (this.filter.countClickReceiverName === 1) {
            this.documents.sort((one, two) => (one.receiverName < two.receiverName ? -1 : 1));
        } else if (this.filter.countClickReceiverName === 2) {
            this.documents.sort((one, two) => (one.receiverName > two.receiverName ? -1 : 1));
        }

        this.container.collectionSize = this.documents.length;
        this.container.currentPage = currentPage;
        this.container.pageSize = sizeElement;

        let startElement = (currentPage - 1) * sizeElement;

        this.documents = this.documents.slice(startElement, startElement + sizeElement);
    }

    loadPage(page: number) {
        if (this.env.production) {
            this.currentProdDocuments(page, this.selectedPageSize.pageSize);
        } else {
            this.currentDevDocuments(page, this.selectedPageSize.pageSize);
        }
    }

    loadPageSize() {
        if (this.env.production) {
            this.currentProdDocuments(this.container.currentPage, this.selectedPageSize.pageSize);
        } else {
            this.currentDevDocuments(this.container.currentPage, this.selectedPageSize.pageSize);
        }
    }

    loadIngoingFormat() {
        if (this.selectedIngoingFormat === null) {
            this.selectedIngoingFormat = {ingoingFormat: 'ALL', selected: true};
        }
        this.filter.ingoingFormat = this.selectedIngoingFormat.ingoingFormat;
        this.filterResult();
    }

    loadTextOrganisation(text: string) {
        if (text.length === 0 || text == null) {
            this.filter.organisation = null;
        } else {
            this.filter.organisation = text;
        }
        this.filterResult();
    }

    loadTextReceiver(text: string) {
        if (text.length === 0 || text == null) {
            this.filter.receiver = null;
        } else {
            this.filter.receiver = text;
        }
        this.filterResult();
    }

    loadStatus() {
        if (this.selectedStatus === null) {
            this.selectedStatus = {status: 'ALL', selected: true};
        }
        this.filter.status = this.selectedStatus.status;
        this.filterResult();
    }

    loadLastErrors() {
        if (this.selectedLastError === null) {
            this.selectedLastError = {lastError: 'ALL', selected: true};
        }
        this.filter.lastError = this.selectedLastError.lastError;
        this.filterResult();
    }

    loadDocumentType() {
        if (this.selectedDocumentType === null) {
            this.selectedDocumentType = {documentType: 'ALL', selected: true};
        }
        this.filter.documentType = this.selectedDocumentType.documentType;
        this.filterResult();
    }

    loadTextSenderName(text: string) {
        if (text.length === 0 || text == null) {
            this.filter.senderName = null;
        } else {
            this.filter.senderName = text;
        }
        this.filterResult();
    }

    loadTextReceiverName(text: string) {
        if (text.length === 0 || text == null) {
            this.filter.receiverName = null;
        } else {
            this.filter.receiverName = text;
        }
        this.filterResult();
    }

    loadReceivedDate(date: Date[]) {
        this.filter.dateReceived = new DateRangeModel(date[0], date[1]);
        this.filterResult();
    }

    loadIssuedDate(date: Date[]) {
        this.filter.dateIssued = new DateRangeModel(date[0], date[1]);
        this.filterResult();
    }

    clickFilter(target: string) {
        if (target === 'Organisation') {
            this.clickOrganisation();
        }
        if (target === 'Receiver') {
            this.clickReceiver();
        }
        if (target === 'Status') {
            this.clickStatus();
        }
        if (target === 'Last Error') {
            this.clickLastError();
        }
        if (target === 'Document Type') {
            this.clickDocumentType();
        }
        if (target === 'Ingoing Format') {
            this.clickIngoingFormat();
        }
        if (target === 'Received') {
            this.clickReceived();
        }
        if (target === 'Issued') {
            this.clickIssued();
        }
        if (target === 'Sender Name') {
            this.clickSenderName();
        }
        if (target === 'Receiver Name') {
            this.clickReceiverName();
        }
        this.filterResult();
    }

    private clickOrganisation() {
        this.filter.countClickOrganisation++;
        if (this.filter.countClickOrganisation > 2) {
            this.filter.countClickOrganisation = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Organisation').columnClick = this.filter.countClickOrganisation;
    }

    private clickReceiver() {
        this.filter.countClickReceiver++;
        if (this.filter.countClickReceiver > 2) {
            this.filter.countClickReceiver = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Receiver').columnClick = this.filter.countClickReceiver;
    }

    private clickStatus() {
        this.filter.countClickStatus++;
        if (this.filter.countClickStatus > 2) {
            this.filter.countClickStatus = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Status').columnClick = this.filter.countClickStatus;
    }

    private clickLastError() {
        this.filter.countClickLastError++;
        if (this.filter.countClickLastError > 2) {
            this.filter.countClickLastError = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Last Error').columnClick = this.filter.countClickLastError;
    }

    private clickDocumentType() {
        this.filter.countClickDocumentType++;
        if (this.filter.countClickDocumentType > 2) {
            this.filter.countClickDocumentType = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Document Type').columnClick = this.filter.countClickDocumentType;
    }

    private clickIngoingFormat() {
        this.filter.countClickIngoingFormat++;
        if (this.filter.countClickIngoingFormat > 2) {
            this.filter.countClickIngoingFormat = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Ingoing Format').columnClick = this.filter.countClickIngoingFormat;
    }

    private clickReceived() {
        this.filter.countClickReceived++;
        if (this.filter.countClickReceived > 2) {
            this.filter.countClickReceived = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Received').columnClick = this.filter.countClickReceived;
    }

    private clickIssued() {
        this.filter.countClickIssued++;
        if (this.filter.countClickIssued > 2) {
            this.filter.countClickIssued = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Issued').columnClick = this.filter.countClickIssued;
    }

    private clickSenderName() {
        this.filter.countClickSenderName++;
        if (this.filter.countClickSenderName > 2) {
            this.filter.countClickSenderName = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Sender Name').columnClick = this.filter.countClickSenderName;
    }

    private clickReceiverName() {
        this.filter.countClickReceiverName++;
        if (this.filter.countClickReceiverName > 2) {
            this.filter.countClickReceiverName = 0;
        }
        this.tableHeaderSortModels.find(k => k.columnName === 'Receiver Name').columnClick = this.filter.countClickReceiverName;
    }

    private filterResult() {
        if (this.selectedPageSize === null) {
            this.selectedPageSize = {pageSize: 10, selected: true};
        }
        this.loadPage(this.container.currentPage);
    }
}
