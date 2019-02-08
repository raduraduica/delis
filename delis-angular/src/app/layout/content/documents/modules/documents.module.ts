import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { TranslateModule } from "@ngx-translate/core";
import { HttpClientModule } from "@angular/common/http";

import { DocumentsRoutingModule } from '../documents-routing.module';
import { DocumentsComponent } from '../components/documents.component';
import { PageHeaderModule } from '../../../../shared';
import { DocumentsService } from '../services/documents.service';
import { JournalDocumentService } from "../../journal/document/services/journal.document.service";
import { BsComponentModule } from "../../../bs-component/bs-component.module";
import { DocumentsOneComponent } from "../components/one/documents.one.component";
import { DocumentsErrorComponent } from "../components/documents.error.component";

@NgModule({
  imports: [
      CommonModule,
      FormsModule,
      NgbModule,
      TranslateModule,
      NgSelectModule,
      DocumentsRoutingModule,
      PageHeaderModule,
      BsDatepickerModule,
      HttpClientModule,
      BsComponentModule],
  declarations: [
      DocumentsComponent,
      DocumentsOneComponent,
      DocumentsErrorComponent],
  providers: [DocumentsService, JournalDocumentService]
})
export class DocumentsModule {}
